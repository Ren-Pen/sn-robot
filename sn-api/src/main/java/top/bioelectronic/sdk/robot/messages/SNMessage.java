package top.bioelectronic.sdk.robot.messages;

import top.bioelectronic.sdk.robot.messages.content.SNAt;
import top.bioelectronic.sdk.robot.messages.content.SNAtAll;
import top.bioelectronic.sdk.robot.messages.content.SNFace;
import top.bioelectronic.sdk.robot.messages.content.SNImage;

public abstract class SNMessage implements Plus {

    public SNMessageChain plus(SNMessage msg){
        return this.toChain().plus(msg);
    }

    @Override
    public SNMessageChain text(String text) {
        return this.toChain().text(text);
    }

    @Override
    public SNMessageChain text(String text, Object... args) {
        return this.toChain().text(text, args);
    }

    public SNMessageChain toChain(){
        return new SNMessageChain().plus(this);
    }

    @Override
    public SNMessageChain atAll() {
        return plus(new SNAtAll());
    }

    @Override
    public SNMessageChain at(long target) {
        return plus(new SNAt(target));
    }

    @Override
    public SNMessageChain face(int id) {
        return plus(new SNFace(id));
    }

    @Override
    public SNMessageChain image(String id) {
        return plus(new SNImage(id));
    }
}
