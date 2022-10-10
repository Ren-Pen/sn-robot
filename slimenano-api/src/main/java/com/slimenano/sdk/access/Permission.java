package com.slimenano.sdk.access;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public enum Permission {

    ROOT(null, Dangerous.DANGEROUS, "所有权限"),
    TEST(ROOT, Dangerous.TEMP, "测试权限"),

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
    private final Permission father;

    @Getter
    private final Dangerous dangerous;

    @Getter
    private final String name;

    Permission(Permission father, Dangerous dangerous, String name) {
        this.father = father;
        this.dangerous = dangerous;
        this.name = name;
    }

    public static Permission[] simplify(Permission[] permissions) {

        HashSet<Permission> set = new HashSet<>(Arrays.asList(permissions));

        for (Permission permission : permissions) {
            if (set.contains(permission.father)) set.remove(permission);
        }

        return set.toArray(new Permission[0]);
    }

    public static Permission[] expends(Permission[] permissions) {

        HashSet<Permission> set = new HashSet<>(Arrays.asList(permissions));

        for (Permission permission : permissions) {
            for (Permission value : Permission.values()) {
                if (permission.isAccess(value)) {
                    set.add(value);
                }
            }
        }

        return set.stream().sorted(Comparator.comparingInt(a -> a.dangerous.getVal())).toArray(Permission[]::new);
    }

    public static String toString(String format, Permission[] permissions) {

        Permission[] expends = expends(permissions);
        StringBuilder builder = new StringBuilder();
        for (Permission permission : expends) {
            builder.append(String.format(format, permission.getDangerous().getTitle(), permission.name));
        }

        return builder.toString();

    }

    /**
     * 判断是否拥有权限
     *
     * @param permission
     *
     * @return
     */
    public boolean isAccess(Permission permission) {
        while (permission != null) {
            if (permission == this) return true;
            permission = permission.father;
        }
        return false;
    }
}
