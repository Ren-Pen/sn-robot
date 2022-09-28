package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.contact.SNContact;

public interface SNUser extends SNContact {

    public SNUserProfile getProfile();

    public void nudge(Robot robot);

}
