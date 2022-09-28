package top.bioelectronic.sdk.robot.contact.user;


public interface SNUserProfile {

    String getNickname();

    String getEmail();

    int getAge();

    int getQLevel();

    Sex getSex();

    /**
     * 个性签名
     */
    String getSign();

    enum Sex {
        MALE,
        FEMALE,

        /**
         * 保密
         */
        UNKNOWN
    }


}
