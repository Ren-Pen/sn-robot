package com.slimenano.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.slimenano.framework.core.BaseRobot;
import com.slimenano.framework.event.EventChannelImpl;
import com.slimenano.sdk.config.ConfigLocation;
import com.slimenano.sdk.framework.BeanContext;
import com.slimenano.sdk.framework.Context;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.ScanPackageLocation;
import com.slimenano.sdk.framework.exception.GetBeanException;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.BasePlugin;

import java.io.File;

@SystemInstance
@ConfigLocation(location = "config")
@ScanPackageLocation("com.slimenano.framework")
@Slf4j
@Marker("应用程序")
public class RobotApplication {

    private static BeanContext context;

    private static Thread guiThread = null;

    private static final Thread cleanup = new Thread(() -> {
        try {
            if (guiThread != null){
                guiThread.interrupt();
            }
            context.getBean(EventChannelImpl.class).addUnregisterActionListener(context, () -> context.close()).unregister(context);
            log.info("等待上下文关闭...");
            while (context.getStatus() != Context.DESTROYED) ;
        } catch (GetBeanException e) {
            e.printStackTrace();
        }
    }, "cleanup");


    public static void main(String[] args) {
        RobotApplication.start(args);
    }


    public static void start(String[] args) {
        try {
            log.info("详细日志：" + new File("logs/latest.log").getAbsolutePath());
            Runtime.getRuntime().addShutdownHook(cleanup);
            context = BeanContext.createContext(RobotApplication.class, null, false);
            IGUIBridge bridge = context.getBean(IGUIBridge.class);
            BaseRobot robot = context.getBean(BaseRobot.class);
            log.info("API版本: {}", BasePlugin.SDK_VERSION);
            log.info("Framework版本: {}", BaseRobot.base_version);
            log.info("已载入的界面桥：{} 版本：{}", bridge.getName(), bridge.getVersion());
            log.info("已载入的核心：{}\n", robot.getCoreType());
            context.refreshAutowiredBean();
            context.notifyLoad();
            guiThread = new Thread(()->{
                try {
                    context.getBean(IGUIBridge.class).main(args);
                } catch (Exception e) {
                    log.error("界面桥运行期间出现异常，应用程序即将终止", e);
                    System.exit(-1);
                }
            });
            guiThread.setName("UI-Main");
            guiThread.start();

        } catch (Exception e) {
            log.error("应用程序初始化时出现异常，初始化程序已被终止", e);
            System.exit(-1);
        }


    }

    @SneakyThrows
    public synchronized static void stop() {
        System.exit(0);
    }


}
