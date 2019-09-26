package com.github.springwind.core.config;

import com.github.springwind.core.factory.EsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/27 13:54
 * @Desc
 */
@Configuration
@Slf4j
@ComponentScan(basePackageClasses = EsClientFactory.class)
public class EsConfig {

    @Value("${elasticsearch.nodes}")
    private List<String> nodes;

    @Value("${elasticSearch.client.connectNum}")
    private Integer connectNum;

    @Value("${elasticSearch.client.connectPerRoute}")
    private Integer connectPerRoute;

    @Value("${elasticsearch.schema}")
    private  String schema;

    @Bean(initMethod = "init", destroyMethod = "close")
    public EsClientFactory esClientFactory() {
        List<HttpHost> httpHosts = new ArrayList<>();
        nodes.forEach(node -> {
            String[] url = node.split(":");
            Assert.notNull(url,"Es Node must be defined");
            Assert.state(url.length == 2,"Es Node must be defined as 'host:port'");
            httpHosts.add(new HttpHost(url[0], Integer.parseInt(url[1]), schema));
        });

        return EsClientFactory.build(httpHosts, connectNum, connectPerRoute);
    }

    @Bean
    @Singleton
    public RestClient restClient() {
        return esClientFactory().getRestClient();
    }

    @Bean
    @Singleton
    public RestHighLevelClient restHighLevelClient() {
        return esClientFactory().getRestHighLevelClient();
    }
}
