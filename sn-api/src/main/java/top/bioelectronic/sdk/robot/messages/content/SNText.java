package top.bioelectronic.sdk.robot.messages.content;

import lombok.Getter;
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
        int n = 0;
        for (int i = 0; i < content.length()-1&&n < objects.length; i++){
            if (content.charAt(i) == '{'){
                if (content.charAt(i+1) == '}'){
                    content = content.substring(0, i) + objects[n++].toString() + content.substring(i+2);
                }
            }
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNText miraiText = (SNText) o;

        return content.equals(miraiText.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
