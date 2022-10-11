package com.slimenano.framework.plugin;

import com.slimenano.framework.commons.XMLReader;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import com.slimenano.framework.access.AccessInterceptor;
import com.slimenano.framework.core.BaseRobot;
import com.slimenano.framework.event.EventChannelImpl;
import com.slimenano.sdk.access.AccessManager;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.BeanContext;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.exception.BeanException;
import com.slimenano.sdk.framework.exception.BeanInitializationException;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.BasePlugin;
import com.slimenano.sdk.plugin.PluginInformation;

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

        if (!BasePlugin.class.isAssignableFrom(pluginClass)) {
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
            String sdk_version = (String) pluginClass.getField("SDK_VERSION").get(null);
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
            try (InputStream is = classLoader.getResourceAsStream("plugin.xml")) {
                long time = System.currentTimeMillis();
                Object emo = XMLReader.deepE2M(XMLReader.parse(is).getDocumentElement());
                log.debug("{} XML文档读取耗时：{}ms", classLoader , System.currentTimeMillis() - time);
                time = System.currentTimeMillis();
                PluginInformation information = XMLReader.EMO2Bean(emo, PluginInformation.class);
                log.debug("{} XML对象转换耗时：{}ms", classLoader , System.currentTimeMillis() - time);
                return information;
            }

        } catch (Exception e) {
            throw new IOException(e);
        }

    }


}
