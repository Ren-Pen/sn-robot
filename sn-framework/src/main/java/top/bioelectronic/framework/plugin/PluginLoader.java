package top.bioelectronic.framework.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import top.bioelectronic.framework.access.AccessInterceptor;
import top.bioelectronic.framework.core.BaseRobot;
import top.bioelectronic.framework.event.EventChannelImpl;
import top.bioelectronic.sdk.access.AccessManager;
import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.framework.BeanContext;
import top.bioelectronic.sdk.framework.SystemInstance;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.framework.exception.BeanException;
import top.bioelectronic.sdk.framework.exception.BeanInitializationException;
import top.bioelectronic.sdk.framework.ui.IGUIBridge;
import top.bioelectronic.sdk.logger.Marker;
import top.bioelectronic.sdk.plugin.BasePlugin;
import top.bioelectronic.sdk.plugin.Plugin;
import top.bioelectronic.sdk.plugin.PluginInformation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@SystemInstance
@Slf4j
@Marker("插件加载器")
public class PluginLoader {

    private static final File dataDir = new File("./data/");
    private static final File pluginDir = new File("./plugins/");


    @Mount
    private AccessManager manager;

    @Mount
    private Robot robot;

    @Mount
    private EventChannelImpl eventChannel;

    @Mount
    private IGUIBridge bridge;

    // 注入内置对象
    public void injection(Class<? extends BasePlugin> pluginClass, BeanContext context, PluginInformation information) throws BeanException {
        log.debug("{} 正在注入插件内置对象", pluginClass);
        // 放置插件预留可操作对象

        if (pluginClass.getAnnotation(Plugin.class) == null) {
            throw new BeanInitializationException(pluginClass + " 不是一个插件类！");
        }

        // 我们需要一个权限管理器，用来管理插件的权限信息
        context.addBean("bot", Enhancer.create(BaseRobot.class, new AccessInterceptor(manager, robot, information)));
        context.addBean("information", information);
        context.addBean("dataDir", new File(dataDir + File.separator + information.getPath()));
        context.addBean("eventChannel", eventChannel);
        context.addBean("iGUIBridge", bridge);
        context.addBean("accessManager", manager);
        // 刷新Context
        context.refreshAutowiredBean();
        // 执行 onLoad
        log.debug("{} 正在执行通知：上下文加载完成", pluginClass);
        context.notifyLoad();

    }

    public Class<? extends BasePlugin> getPluginClass(ClassLoader classLoader, PluginInformation information) throws ClassNotFoundException {
        Class<? extends BasePlugin> pluginClass = (Class<? extends BasePlugin>) classLoader.loadClass(information.getPath());
        try {
            String sdk_version = (String) pluginClass.getDeclaredField("SDK_VERSION").get(null);
            log.debug("{} 插件目标SDK版本：{}", pluginClass, sdk_version);
            if (!BasePlugin.SDK_VERSION.equals(sdk_version)) {
                log.warn("{} 非目标版本的插件，可能会造成运行异常！", pluginClass);
            }


        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            log.warn("{} 无法获取插件SDK版本信息！", pluginClass);
        }


        return pluginClass;
    }

    /**
     * 打开一个类加载器
     *
     * @param jarFile
     *
     * @return
     */
    public DynamicJarClassLoader open(File jarFile) {

        if (!jarFile.exists()) {
            log.warn("{} 插件文件不存在", jarFile.getName());
            return null;
        }

        return new DynamicJarClassLoader(jarFile, null);
    }

    /**
     * 获取插件的描述信息
     *
     * @param classLoader
     *
     * @return
     *
     * @throws IOException
     */
    public PluginInformation getInformation(ClassLoader classLoader) throws IOException {

        try {
            try (InputStream is = classLoader.getResourceAsStream("plugin.json")) {
                ObjectMapper om = new ObjectMapper();
                return om.readValue(is, PluginInformation.class);
            }

        } catch (Exception e) {
            throw new IOException(e);
        }

    }


}
