package top.bioelectronic.sdk.robot.messages.content;

import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNContentMessage;

@Getter
public class SNAt extends SNContentMessage {

    private final long target;

    public SNAt(long target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNAt miraiAt = (SNAt) o;

        return target == miraiAt.target;
    }

    @Override
    public int hashCode() {
        return (int) (target ^ (target >>> 32));
    }
}
