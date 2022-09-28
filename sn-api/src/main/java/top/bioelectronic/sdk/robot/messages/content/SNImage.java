package top.bioelectronic.sdk.robot.messages.content;

import lombok.Getter;
import lombok.Setter;
import top.bioelectronic.sdk.robot.contact.SNContact;
import top.bioelectronic.sdk.robot.messages.SNContentMessage;

import java.util.regex.Pattern;

@Getter
public class SNImage extends SNContentMessage {

    private static final Pattern pattern = Pattern.compile("\\{[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}\\}\\..{3,5}");
    private final String queryUrl;
    private final String imageId;
    private final String imageType;
    private final int height;
    private final int width;
    private final long size;
    private final byte[] md5;

    @Setter
    private SNContact contact = null;


    public SNImage(String queryUrl, String imageId, String imageType, int height, int width, long size, byte[] md5) {
        this.queryUrl = queryUrl;
        this.imageId = imageId;
        this.imageType = imageType;
        this.height = height;
        this.width = width;
        this.size = size;
        this.md5 = md5;
    }

    public SNImage(String imageId) {
        this.imageId = imageId;
        this.queryUrl = null;
        this.imageType = null;
        this.height = 0;
        this.width = 0;
        this.size = 0;
        this.md5 = new byte[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNImage that = (SNImage) o;

        return imageId.equals(that.imageId);
    }

    @Override
    public int hashCode() {
        return imageId.hashCode();
    }
}
