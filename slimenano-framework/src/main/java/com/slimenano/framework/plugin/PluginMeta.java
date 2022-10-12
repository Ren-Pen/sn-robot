package com.slimenano.framework.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.slimenano.nscan.framework.BeanContext;
import com.slimenano.sdk.plugin.BasePlugin;
import com.slimenano.sdk.plugin.PluginInformation;

import java.io.File;

/**
 * 插件元数据
 */
@Data
@AllArgsConstructor
public class PluginMeta {

    /**
     * 插件文件
     */
    private File jarFile;

    /**
     * 插件上下文
     */
    private BeanContext context;

    /**
     * 插件类
     */
    private BasePlugin plugin;

    /**
     * 插件类加载器
     */
    private DynamicJarClassLoader pluginLoader;

    /**
     * 插件信息
     */
    private PluginInformation information;

    /**
     * 插件文件sha1值
     */
    private String sha1;

}
