package com.slimenano.framework.plugin;

import com.slimenano.framework.event.impl.plugin.PluginUnloadedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.slimenano.framework.access.AccessManagerImpl;
import com.slimenano.framework.config.RobotConfiguration;
import com.slimenano.framework.event.EventChannelImpl;
import com.slimenano.framework.event.impl.plugin.PluginLoadFailEvent;
import com.slimenano.framework.event.impl.plugin.PluginLoadedEvent;
import com.slimenano.framework.event.impl.plugin.PluginPreLoadEvent;
import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.framework.BeanContext;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.exception.BeanException;
import com.slimenano.sdk.framework.ui.GUI_CONST;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.BasePlugin;
import com.slimenano.sdk.plugin.PluginInformation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@SystemInstance
@Slf4j
@Marker("插件管理器")
public class PluginManager {

    private static final File pluginDir = new File("./plugins/");
    private static final File dataDir = new File("./data/");

    @Getter
    private final HashMap<String, PluginMeta> pluginMap = new HashMap<>();

    @Mount
    private PluginLoader pluginLoader;

    @Mount("config:robot")
    private RobotConfiguration configuration;

    @Mount
    private EventChannelImpl eventChannel;

    @Mount
    private IGUIBridge bridge;

    @Mount
    private AccessManagerImpl accessManager;

    public PluginManager(){
        pluginDir.mkdirs();
        dataDir.mkdirs();
    }

    public boolean load(String jarFileName){

        BeanContext context = null;

        if (configuration.isSimplyJarFile()) {
            if (!jarFileName.endsWith(".jar")) {
                jarFileName += ".jar";
                log.debug("{} 文件名已修正", jarFileName);
            }

        }

        File jarFile = new File(pluginDir + File.separator + jarFileName);

        DynamicJarClassLoader classLoader = pluginLoader.open(jarFile);
        if (classLoader == null) return false;

        log.debug("{} 插件类加载器以创建，插件文件已打开。加载器：{}", jarFileName, classLoader);
        PluginInformation information = null;
        try {
            information = pluginLoader.getInformation(classLoader);
            log.debug("{} 插件描述信息已读取：{}", information.getPath(), information);

            if (information.getPath().startsWith("com.slimenano")){
                throw new IllegalAccessException("由于安全性设置，应用程序禁止任何以com.slimenano为包名的类作为插件加载，请更换您的包名后重新构建插件！");
            }

            // 检查冲突
            if (pluginMap.containsKey(information.getPath())) {
                String conditionJarFileName = pluginMap.get(information.getPath()).getJarFile().getName();
                log.debug("{} 插件类冲突！冲突插件：{}", jarFileName, conditionJarFileName);
                throw new RuntimeException("插件加载时出现冲突，冲突插件：" + conditionJarFileName);
            }

            eventChannel.post(new PluginPreLoadEvent(information));

            if (!bridge.confirm("加载到未知插件",
                    "插件名：" + information.getName() + "\n" +
                            "插件类名：" + information.getPath() + "\n" +
                            "插件作者：" + information.getAuthor() + "\n" +
                            "插件版本：" + information.getVersion() + "\n" +
                            "插件详情：" + information.getDescription() + "\n" +
                            "插件权限：\n" + Permission.toString(information.getPermissions()) + "\n" +
                            "插件管理器无法识别插件是否安全，是否加载？"
                    , GUI_CONST.YES_NO)){
                log.debug("{} 插件加载行为已被取消", information.getPath());
                throw new BeanException("用户拒绝载入！");
            }
            // 注册插件权限
            accessManager.registerPlugin(information);

            Class<? extends BasePlugin> pluginClass = pluginLoader.getPluginClass(classLoader, information);
            log.debug("{} 插件类已成功加载，即将创建插件上下文", pluginClass);
            context = BeanContext.createContext(pluginClass, classLoader, false);
            pluginLoader.injection(pluginClass, context, information);
            BasePlugin bean = context.getBean(BasePlugin.class);
            PluginMeta meta = new PluginMeta(jarFile, context, bean, classLoader, information);

            // 注册插件到事件总线
            log.debug("插件加载器 | {} 正在注册插件上下文事件", pluginClass);
            eventChannel.register(context);

            pluginMap.put(information.getPath(), meta);
            try {
                bean.loaded();
            }catch (Exception e){
                log.error("插件在后加载步骤出现异常，可能会导致插件无法正常运行，请卸载插件后重试！", e);
            }
            eventChannel.post(new PluginLoadedEvent(meta));


        } catch (Exception e) {
            log.error("{} 加载插件时出现了错误", jarFileName, e);
            eventChannel.post(new PluginLoadFailEvent(information));
            if (context != null) {
                BeanContext finalContext = context;
                eventChannel.addUnregisterActionListener(context, () -> BeanContext.removeContext(finalContext)).unregister(finalContext);
            }
            try {
                classLoader.close();
            } catch (IOException ex) {
                log.error("{} 卸载类加载器时出现错误", jarFileName, ex);
                return false;
            }
            bridge.alert("插件管理器", "插件 " + jarFileName + " 加载失败！\n原因：" + e.getMessage(), GUI_CONST.ERROR);
            return false;
        }

        return true;

    }

    public void unload(String className) {
        log.debug("{} 准备卸载插件", className);
        if (!pluginMap.containsKey(className)) {
            log.warn("{} 插件不存在", className);
            return;
        }

        PluginMeta meta = pluginMap.remove(className);
        BeanContext context = meta.getContext();
        eventChannel.addUnregisterActionListener(context, () -> {
            BeanContext.removeContext(context);
            try {
                meta.getPluginLoader().close();
            } catch (IOException e) {
                log.error("{} 卸载类加载器时出现错误", meta.getPluginLoader(), e);
                bridge.alert("插件管理器", "插件卸载时出现错误，请注意检查日志", GUI_CONST.ERROR);
            }
        }).unregister(context);

        log.debug("{} 插件已卸载", className);
        eventChannel.post(new PluginUnloadedEvent(meta));
        bridge.alert("插件管理器", "插件 " + meta.getJarFile().getName() + " 已卸载", GUI_CONST.INFO);
    }


}
