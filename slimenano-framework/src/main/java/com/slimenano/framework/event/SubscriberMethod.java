package com.slimenano.framework.event;

import com.slimenano.sdk.event.IEvent;

import java.lang.reflect.Method;

public class SubscriberMethod {

    final private Method method;
    final private Class<? extends IEvent> eventType;
    final private int priority;
    final private Object o;


    public SubscriberMethod(Object o, Method method, Class<? extends IEvent> eventType, int priority) {
        this.o = o;
        this.method = method;
        this.priority = priority;
        this.method.setAccessible(true);
        this.eventType = eventType;
        this.method.setAccessible(false);
    }

    public int getPriority() {
        return priority;
    }

    public Class<? extends IEvent> getEventType() {
        return eventType;
    }

    public Method getMethod() {
        return method;
    }

    public Object getO() {
        return o;
    }
}
