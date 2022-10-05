package com.slimenano.framework.event.impl;

import com.slimenano.sdk.event.IEvent;

/**
 * 系统事件，非系统监听器将不会接收
 */
public abstract class ISysEvent<T> extends IEvent<T> {
    public ISysEvent(T source) {
        super(source);
    }
}
