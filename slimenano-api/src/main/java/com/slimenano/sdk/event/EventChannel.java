package com.slimenano.sdk.event;

public interface EventChannel {
    void post(IEvent event);

    void postSync(IEvent event);

    void dead(Class<?> clazz);
}
