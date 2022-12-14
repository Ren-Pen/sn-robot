package com.slimenano.framework.access;

import com.slimenano.framework.commons.ClassUtils;
import com.slimenano.framework.core.AbstractRobot;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedStatusException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.access.AccessManager;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.annotations.AccessControl;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.PluginInformation;

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
        method.setAccessible(false);

        AccessControl accessControl = ClassUtils.getMethodAnnotation(method, AccessControl.class);
        if (accessControl == null){
            return method.invoke(robot, objects);
        }else{
            if (accessControl.status() && !robot.getStatus()){
                log.debug("{} 非法的状态执行：{}", information.getPath(), method.getName());
                throw new UnsupportedStatusException();
            }
            if (manager.canAccess(information, accessControl.require())) {
                log.debug("{} 放行插件执行方法：{}", information.getPath(), method.getName());
                return method.invoke(robot, objects);
            }else{
                log.warn("{} 插件行为未授权：{} 需要权限：{}", information.getPath(), method.getName(), Permission.toString("[%s] %s%n", accessControl.require()));
                throw new NoOperationPermissionException(accessControl.require());
            }
        }



    }
}
