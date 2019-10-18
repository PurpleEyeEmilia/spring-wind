package com.github.springwind.core.canal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/6 15:44
 * @Desc
 */
@EnableConfigurationProperties(CanalProperties.class)
@ConfigurationProperties(prefix = "canal")
@Data
public class CanalProperties {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * zk地址
     */
    private String zkServers;

    /**
     * 实例名
     */
    private String instance;

    /**
     * 用户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
