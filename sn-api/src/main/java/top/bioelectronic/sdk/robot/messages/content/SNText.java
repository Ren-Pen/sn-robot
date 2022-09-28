package top.bioelectronic.sdk.robot.messages.content;

import lombok.Getter;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import top.bioelectronic.sdk.robot.messages.SNContentMessage;

@Getter
public class SNText extends SNContentMessage {

    private final String content;

    public SNText(String content) {
        if (content == null) throw new NullPointerException("Content cannot be null!");
        this.content = content;
    }

    public SNText(String content, Object... objects) {
        if (content == null) throw new NullPointerException("Content cannot be null!");
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(content, objects);
        this.content = formattingTuple.getMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNText snText = (SNText) o;

        return content.equals(snText.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
