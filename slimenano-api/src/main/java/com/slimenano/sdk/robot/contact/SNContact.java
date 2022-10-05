package com.slimenano.sdk.robot.contact;


import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

public interface SNContact {

    public long getId();

    SNMessageSource sendMessage(Robot robot, SNMessageChain chain);

}
