package top.bioelectronic.sdk.robot.messages;

public interface Plus {

    SNMessageChain plus(SNMessage message);

    SNMessageChain text(String text);

    SNMessageChain text(String text, Object... args);

    SNMessageChain atAll();

    SNMessageChain at(long target);

    SNMessageChain face(int id);

    SNMessageChain image(String id);

}
