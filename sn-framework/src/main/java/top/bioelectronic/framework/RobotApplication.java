package top.bioelectronic.framework;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.bioelectronic.framework.core.BaseRobot;
import top.bioelectronic.framework.event.EventChannelImpl;
import top.bioelectronic.sdk.config.ConfigLocation;
import top.bioelectronic.sdk.framework.BeanContext;
import top.bioelectronic.sdk.framework.Context;
import top.bioelectronic.sdk.framework.SystemInstance;
import top.bioelectronic.sdk.framework.annotations.ScanPackageLocation;
import top.bioelectronic.sdk.framework.exception.BeanException;
import top.bioelectronic.sdk.framework.exception.GetBeanException;
import top.bioelectronic.sdk.framework.ui.IGUIBridge;
import top.bioelectronic.sdk.framework.ui.StartCallback;
import top.bioelectronic.sdk.logger.Marker;
import top.bioelectronic.sdk.plugin.BasePlugin;

import java.io.File;

@SystemInstance
@ConfigLocation(location = "config")
@ScanPackageLocation("top.bioelectronic.framework")
@Slf4j
@Marker("应用程序")
public class RobotApplication {

    private static BeanContext context;

    private static final Thread cleanup = new Thread(() -> {
        try {
            context.getBean(EventChannelImpl.class).addUnregisterActionListener(context, () -> context.close()).unregister(context);
            log.info("等待上下文关闭...");
            while (context.getStatus() != Context.DESTROYED) ;
        } catch (GetBeanException e) {
            e.printStackTrace();
        }
    }, "cleanup");


    public static void main(String[] args) throws BeanException {
        log.info("详细日志：" + new File("logs/latest.log").getAbsolutePath());
        Context context = RobotApplication.start();
        Runtime.getRuntime().addShutdownHook(cleanup);
        context.getBean(IGUIBridge.class).main(args);
    }

    public static Context start() throws BeanException {
        return start(null);
    }

    public static Context start(StartCallback callback) throws BeanException {
        try {
            context = BeanContext.createContext(RobotApplication.class, null, false);
            IGUIBridge bridge = context.getBean(IGUIBridge.class);
            BaseRobot robot = context.getBean(BaseRobot.class);
            log.info("API版本: {}", BasePlugin.SDK_VERSION);
            log.info("Framework版本: {}", BaseRobot.base_version);
            log.info("已载入的界面桥：{} 版本：{}", bridge.getName(), bridge.getVersion());
            log.info("已载入的核心：{}\n", robot.getCoreType());
            if (callback != null)
                callback.preLoad(context);
            context.refreshAutowiredBean();
            context.notifyLoad();

        } catch (Exception e) {
            log.error("应用程序初始化时出现异常，初始化程序已被终止", e);
            System.exit(-1);
        }

        if (callback != null) {
            callback.postLoad(context);
        }

        return context;


    }

    @SneakyThrows
    public synchronized static void stop() {
        System.exit(0);
    }


}
