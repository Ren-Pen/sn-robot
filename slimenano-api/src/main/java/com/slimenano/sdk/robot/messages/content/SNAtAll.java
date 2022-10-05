package com.slimenano.sdk.robot.messages.content;

import com.slimenano.sdk.robot.messages.SNContentMessage;

public class SNAtAll extends SNContentMessage {


    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == SNAtAll.class;
    }
}
