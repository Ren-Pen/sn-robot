package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;

public class SNStrangerImpl extends SNUserImpl implements SNStranger {
    public SNStrangerImpl(long id, SNUserProfile profile) {
        super(id, profile);
    }

    @Override
    public SNMessageSource sendMessage(Robot robot, SNMessageChain chain) {
        return robot.sendMessage(this, chain);
    }

}
