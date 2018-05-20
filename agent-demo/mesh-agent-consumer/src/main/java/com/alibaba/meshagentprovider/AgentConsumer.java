package com.alibaba.meshagentprovider;

import com.alibaba.meshagentprovider.registry.Endpoint;
import com.alibaba.meshagentprovider.registry.EtcdRegistry;
import com.alibaba.meshagentprovider.registry.IRegistry;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:01 2018/5/19.
 */
@RestController
public class AgentConsumer {
    private Logger logger = LoggerFactory.getLogger(AgentConsumer.class);

    private Random random = new Random();
    private static List<Endpoint> endpoints = null;
    private static IRegistry registry = new EtcdRegistry(System.getProperty("etcd.url"));
    private static AsyncHttpClient asyncHttpClient1 = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
            .setConnectTimeout(10000)
            .setRequestTimeout(10000)
            .setKeepAlive(true)
            .build());
    private static AsyncHttpClient asyncHttpClient2 = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
            .setConnectTimeout(10000)
            .setRequestTimeout(10000)
            .setKeepAlive(true)
            .build());
    private static AsyncHttpClient asyncHttpClient3 = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
            .setConnectTimeout(10000)
            .setRequestTimeout(10000)
            .setKeepAlive(true)
            .build());
    private static List<AsyncHttpClient> asyncHttpClients = new ArrayList<>();

    static {
        if (null == endpoints){
            try {
                endpoints = registry.find("com.alibaba.dubbo.performance.demo.provider.IHelloService");
                asyncHttpClients.add(asyncHttpClient1);
                asyncHttpClients.add(asyncHttpClient2);
                asyncHttpClients.add(asyncHttpClient3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "")
    public Object invoke(@RequestParam("interface") String interfaceName,
                         @RequestParam("method") String method,
                         @RequestParam("parameterTypesString") String parameterTypesString,
                         @RequestParam("parameter") String parameter) throws Exception {

        return consumer(interfaceName,method,parameterTypesString,parameter);
    }

    public Integer consumer(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {

        // 简单的负载均衡，顺序获取
        int a = 2;
        Endpoint endpoint = endpoints.get(a);

        String url =  "http://" + endpoint.getHost() + ":" + endpoint.getPort();

        RequestBuilder builder = new RequestBuilder();
        builder.setUrl(url);
        builder.addQueryParam("interface",interfaceName);
        builder.addQueryParam("method",method);
        builder.addQueryParam("parameterTypesString",parameterTypesString);
        builder.addQueryParam("parameter",parameter);
        Future<Response> f;
        Response response;

        f = asyncHttpClients.get(a).executeRequest(builder.build());
        response = f.get();
        return Integer.valueOf(new String(response.getResponseBody().getBytes()));

    }

}
