package com.slimenano.sdk.robot.messages.content;

import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNContentMessage;

@Getter
public class SNFace extends SNContentMessage {


    private final int id;
    private final String name;

    public SNFace(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SNFace(int id) {
        this.id = id;
        this.name = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNFace miraiFace = (SNFace) o;

        return id == miraiFace.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
