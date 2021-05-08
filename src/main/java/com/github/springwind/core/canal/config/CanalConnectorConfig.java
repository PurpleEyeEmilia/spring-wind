package com.github.springwind.core.canal.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.github.springwind.core.canal.service.CanalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/6 16:15
 * @Desc
 */
@Configuration
@ConditionalOnClass({CanalConnector.class, CanalConnectors.class})
@EnableConfigurationProperties(CanalProperties.class)
@Slf4j
public class CanalConnectorConfig {

    private CanalProperties canalProperties;

    public CanalConnectorConfig(CanalProperties canalProperties) {
        this.canalProperties = canalProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "canal", name = {"host", "port", "instance", "username", "password"}, matchIfMissing = false)
    public CanalConnector singleton() {
        return CanalConnectors.newSingleConnector(new InetSocketAddress(canalProperties.getHost(), canalProperties.getPort()), canalProperties.getInstance(), canalProperties.getUsername(), canalProperties.getPassword());
    }

    @Bean
    @ConditionalOnProperty(prefix = "canal", name = {"zkServes", "instance", "username", "password"}, matchIfMissing = false)
    public CanalConnector cluster() {
        return CanalConnectors.newClusterConnector(canalProperties.getZkServers(), canalProperties.getInstance(), canalProperties.getUsername(), canalProperties.getPassword());
    }

    @Bean
    @ConditionalOnBean(CanalConnector.class)
    public CanalService canalService() {
        return new CanalService();
    }

}