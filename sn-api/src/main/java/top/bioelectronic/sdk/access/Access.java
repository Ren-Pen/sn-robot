package top.bioelectronic.sdk.access;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public enum Access {

    ROOT(null, Dangerous.DANGEROUS, "所有权限"),
    TEST(ROOT, Dangerous.TEMP, "测试权限"),
    TEST2(TEST, Dangerous.TEMP, "测试权限2"),

    BEHAVIOR(ROOT, Dangerous.DANGEROUS, "基础行为权限"),
    BEHAVIOR_GET_BOT_ID(BEHAVIOR, Dangerous.NORMAL, "获取当前登录ID权限"),
    BEHAVIOR_IMG_UPLOAD(BEHAVIOR, Dangerous.PRIVACY, "上传图片权限"),
    BEHAVIOR_RECALL(BEHAVIOR, Dangerous.PRIVACY, "消息撤回权限"),
    BEHAVIOR_NUDGE(BEHAVIOR, Dangerous.NORMAL, "戳一戳权限"),
    BEHAVIOR_KICK_MEMBER(BEHAVIOR, Dangerous.DANGEROUS, "移除群成员权限"),
    BEHAVIOR_MUTE_MEMBER(BEHAVIOR, Dangerous.PRIVACY, "禁言/解禁群成员权限"),


    SEND(ROOT, Dangerous.DANGEROUS, "发送消息父权限"),

    SEND_FRIEND(SEND, Dangerous.NORMAL, "发送好友消息权限"),
    SEND_GROUP(SEND, Dangerous.NORMAL, "发送群聊消息权限"),
    SEND_STRANGER(SEND, Dangerous.DANGEROUS, "发送陌生人消息权限"),
    SEND_GROUP_MEMBER(SEND, Dangerous.DANGEROUS, "发送临时会话消息权限"),


    GET(ROOT, Dangerous.DANGEROUS, "获取用户父权限"),

    GET_FRIENDS(GET, Dangerous.PRIVACY, "获取好友列表"),
    GET_GROUPS(GET, Dangerous.PRIVACY, "获取群列表"),
    GET_GROUP_MEMBERS(GET, Dangerous.PRIVACY, "获取群成员列表"),

    GET_FRIEND(GET_FRIENDS, Dangerous.PRIVACY, "查询好友信息"),
    GET_GROUP(GET_GROUPS, Dangerous.PRIVACY, "查询群组信息"),
    GET_GROUP_MEMBER(GET_GROUP_MEMBERS, Dangerous.PRIVACY, "查询群成员信息"),
    GET_STRANGER(GET, Dangerous.PRIVACY, "查询陌生人信息"),
    ;

    @Getter
    private final Access father;

    @Getter
    private final Dangerous dangerous;

    @Getter
    private final String name;

    Access(Access father, Dangerous dangerous, String name) {
        this.father = father;
        this.dangerous = dangerous;
        this.name = name;
    }

    public static Access[] simplify(Access[] accesses) {

        HashSet<Access> set = new HashSet<>(Arrays.asList(accesses));

        for (Access access : accesses) {
            if (set.contains(access.father)) set.remove(access);
        }

        return set.toArray(new Access[0]);
    }

    public static Access[] expends(Access[] accesses) {

        HashSet<Access> set = new HashSet<>(Arrays.asList(accesses));

        for (Access access : accesses) {
            for (Access value : Access.values()) {
                if (access.isAccess(value)) {
                    set.add(value);
                }
            }
        }

        return set.stream().sorted(Comparator.comparingInt(a -> a.dangerous.getVal())).toArray(Access[]::new);
    }

    public static String toString(Access[] accesses) {

        Access[] expends = expends(accesses);
        StringBuilder builder = new StringBuilder();
        for (Access access : expends) {
            builder.append("[")
                    .append(access.getDangerous().getTitle())
                    .append("] ")
                    .append(access.name).append("\n");
        }

        return builder.toString();

    }

    /**
     * 判断是否拥有权限
     *
     * @param access
     *
     * @return
     */
    public boolean isAccess(Access access) {
        while (access != null) {
            if (access == this) return true;
            access = access.father;
        }
        return false;
    }
}
