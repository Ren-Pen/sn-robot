package top.bioelectronic.framework.access;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import top.bioelectronic.sdk.access.Access;
import top.bioelectronic.sdk.access.AccessManager;
import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.framework.annotations.AccessControl;
import top.bioelectronic.sdk.logger.Marker;
import top.bioelectronic.sdk.plugin.PluginInformation;

import java.lang.reflect.Method;

@Slf4j
@Marker("权限管理器")
public class AccessInterceptor implements MethodInterceptor {

    private final AccessManager manager;
    private final Robot robot;
    private final PluginInformation information;

    public AccessInterceptor(AccessManager manager, Robot robot, PluginInformation information) {
        this.manager = manager;
        this.robot = robot;
        this.information = information;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        AccessControl accessControl = method.getAnnotation(AccessControl.class);
        method.setAccessible(false);
        if (accessControl == null){
            return method.invoke(robot, objects);
        }else{

            if (manager.canAccess(information, accessControl.require())) {
                log.debug("{} 放行插件执行方法：{}", information.getPath(), method.getName());
                return method.invoke(robot, objects);
            }else{
                log.warn("{} 插件行为未授权：{} 需要权限：{}", information.getPath(), method.getName(), Access.toString(accessControl.require()));
                return null;
            }
        }



    }
}
