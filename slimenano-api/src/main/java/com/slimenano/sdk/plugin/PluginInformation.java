package com.slimenano.sdk.plugin;

import com.slimenano.sdk.framework.annotations.Instance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.slimenano.sdk.access.Access;

import java.util.HashMap;

@Data
@Instance
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PluginInformation {

    /**
     * 插件路径
     */
    private String path;

    /**
     * 插件名称
     */
    private String name;

    /**
     * 插件版本
     */
    private String version;

    /**
     * 插件作者
     */
    private String author;

    /**
     * 插件描述
     */
    private String description;

    /**
     * 插件申请权限
     */
    private Access[] accesses;

    /**
     * 扩展数据
     */
    private HashMap<String, Object> extension;

}
