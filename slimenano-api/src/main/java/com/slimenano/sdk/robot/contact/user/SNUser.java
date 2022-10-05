package com.slimenano.sdk.robot.contact.user;

import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.contact.SNContact;

public interface SNUser extends SNContact {

    public SNUserProfile getProfile();

    public void nudge(Robot robot);

}
