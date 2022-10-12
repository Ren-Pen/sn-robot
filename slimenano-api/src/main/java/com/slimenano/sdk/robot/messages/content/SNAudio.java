package com.slimenano.sdk.robot.messages.content;

import com.slimenano.sdk.robot.messages.SNContentMessage;
import lombok.Getter;

import java.util.Arrays;

/**
 * 语音消息
 */
@Getter
public class SNAudio extends SNContentMessage {

    private final String filename;
    private final byte[] fileMd5;
    private final long fileSize;
    private final String codec;
    private final byte[] extra;
    private final String url;


    public SNAudio(String filename, byte[] fileMd5, long fileSize, String codec, byte[] extra, String url) {
        this.filename = filename;
        this.fileMd5 = fileMd5;
        this.fileSize = fileSize;
        this.codec = codec;
        this.extra = extra;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNAudio snAudio = (SNAudio) o;

        if (fileSize != snAudio.fileSize) return false;
        if (!filename.equals(snAudio.filename)) return false;
        if (!Arrays.equals(fileMd5, snAudio.fileMd5)) return false;
        if (!codec.equals(snAudio.codec)) return false;
        if (!Arrays.equals(extra, snAudio.extra)) return false;
        return url.equals(snAudio.url);
    }

    @Override
    public int hashCode() {
        int result = filename.hashCode();
        result = 31 * result + Arrays.hashCode(fileMd5);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + codec.hashCode();
        result = 31 * result + Arrays.hashCode(extra);
        result = 31 * result + url.hashCode();
        return result;
    }
}
