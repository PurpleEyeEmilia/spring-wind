package com.github.springwind.core.factory;

import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/8/21 16:26
 * @Desc
 */
@Slf4j
public class EsClientFactory {
//
//    private static int connectTimeOutMillisecond = 1000;
//
//    private static int socketTimeOutMillisecond = 3000;
//
//    private static int connectRequestTimeOutMillisecond = 500;
//
//    private static int maxConnectPerRoute = 10;
//
//    private static int maxConnectTotal = 30;
//
//    private static List<HttpHost> httpHosts;
//
//    private static EsClientFactory esClientFactory = new EsClientFactory();
//
//    private RestClientBuilder restClientBuilder;
//
//    private RestClient restClient;
//
//    private RestHighLevelClient restHighLevelClient;
//
//    public static EsClientFactory build(List<HttpHost> httpHosts, Integer maxConnectTotal, Integer maxConnectPerRoute) {
//        EsClientFactory.httpHosts = httpHosts;
//        EsClientFactory.maxConnectTotal = maxConnectTotal;
//        EsClientFactory.maxConnectPerRoute = maxConnectPerRoute;
//        return esClientFactory;
//    }
//
//    private EsClientFactory() {
//    }
//
//    public static EsClientFactory build(List<HttpHost> httpHosts, Integer connectTimeOut, Integer socketTimeOut,
//                                        Integer connectionRequestTime, Integer maxConnectTotal, Integer maxConnectPerRoute) {
//        EsClientFactory.httpHosts = httpHosts;
//        connectTimeOutMillisecond = connectTimeOut;
//        socketTimeOutMillisecond = socketTimeOut;
//        connectRequestTimeOutMillisecond = connectionRequestTime;
//        EsClientFactory.maxConnectTotal = maxConnectTotal;
//        EsClientFactory.maxConnectPerRoute = maxConnectPerRoute;
//        return esClientFactory;
//    }
//
//    public void init() {
//        restClientBuilder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));
//        setConnectTimeoutConfig();
//        setConcurrentConnectConfig();
//        restClient = restClientBuilder.build();
//        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//        log.info("init Es factory!");
//    }
//
//    /**
//     * 设置并发连接数
//     */
//    private void setConcurrentConnectConfig() {
//        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
//            httpClientBuilder.setMaxConnTotal(maxConnectTotal);
//            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
//            return httpClientBuilder;
//        });
//    }
//
//    /**
//     * 设置超时时间
//     */
//    private void setConnectTimeoutConfig() {
//        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
//            requestConfigBuilder.setConnectTimeout(connectTimeOutMillisecond);
//            requestConfigBuilder.setConnectionRequestTimeout(connectRequestTimeOutMillisecond);
//            requestConfigBuilder.setSocketTimeout(socketTimeOutMillisecond);
//            return requestConfigBuilder;
//        });
//    }
//
//    public RestClient getRestClient() {
//        return restClient;
//    }
//
//    public RestHighLevelClient getRestHighLevelClient() {
//        return restHighLevelClient;
//    }
//
//    public void close() {
//        if (restClient != null) {
//            try {
//                restClient.close();
//                log.info("关闭restClient！");
//            } catch (Exception e) {
//                log.error("关闭restClient出错！", e);
//            }
//        }
//    }
}
