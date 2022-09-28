package top.bioelectronic.sdk.robot.contact;


import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;

public interface SNContact {

    public long getId();

    SNMessageSource sendMessage(Robot robot, SNMessageChain chain);

}
