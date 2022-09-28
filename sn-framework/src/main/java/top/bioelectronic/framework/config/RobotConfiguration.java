package top.bioelectronic.framework.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.bioelectronic.sdk.config.BaseConfiguration;
import top.bioelectronic.sdk.config.Configuration;
import top.bioelectronic.sdk.config.DefaultConfiguration;
import top.bioelectronic.sdk.framework.SystemInstance;

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

    private long account;
    private String password;
    private String protocol;
    private boolean simplyJarFile;
    private boolean rememberPassword;

    @Override
    public DefaultConfiguration createDefaultConfigurationObject() {
        return new RobotConfiguration(0L, "", "MACOS", true, false);
    }
}
