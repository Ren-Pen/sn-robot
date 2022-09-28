package top.bioelectronic.sdk.event;

import top.bioelectronic.sdk.common.Nullable;
import lombok.Getter;

@Getter
public abstract class IEvent<T> {

    // 负载，事件自定义携带的对象
    @Nullable
    private final T payload;
    // 事件被取消，事件取消仅对同类型的事件监听器有效。
    private boolean prevent = false;

    public IEvent(T payload) {
        this.payload = payload;
    }

    public void prevent() {
        this.prevent = true;
    }


}
