package com.slimenano.sdk.robot.messages.content;

import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNContentMessage;

import java.util.Objects;

@Getter
public class SNFlashImage extends SNContentMessage {

    private final SNImage image;

    public SNFlashImage(SNImage image) {
        this.image = image;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNFlashImage that = (SNFlashImage) o;

        return Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return image != null ? image.hashCode() : 0;
    }
}
