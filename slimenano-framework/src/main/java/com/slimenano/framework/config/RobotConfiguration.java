package com.slimenano.framework.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.slimenano.sdk.config.BaseConfiguration;
import com.slimenano.sdk.config.Configuration;
import com.slimenano.sdk.config.DefaultConfiguration;
import com.slimenano.sdk.framework.SystemInstance;

/**
 * 机器人配置项
 * 账号
 * 密码
 * 协议
 * 简化jar包路径名
 * 是否记住密码
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SystemInstance
@Configuration(prefix = "robot")
public class RobotConfiguration extends BaseConfiguration implements DefaultConfiguration {

    private long account = 0L;
    private String password = "";
    private String protocol = "MACOS";
    private boolean rememberPassword = false;

}
