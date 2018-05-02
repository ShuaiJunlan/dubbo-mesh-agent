package cn.shuaijunlan.xagent.http;

import cn.shuaijunlan.xagent.httpserver.HttpServerBootstrap;
import org.junit.Test;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 16:34 2018/4/28.
 */
public class HttpServerBootstrapTest {

    @Test
    public void start() {
        HttpServerBootstrap httpServerBootstrap = new HttpServerBootstrap();
        try {
            httpServerBootstrap.start(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test(){
       String str = "SMSNOd53QsF9lO3LE0AnVaDlDAHhidqTJNIbtFxwz7BFFrVQ4SGP1aYIdTsA37jFbUV8opxG4FEZX7fuDlZwo2YYooIXOewlXHLq9YdWg3uNCca2PriRYAbLFHjOfnALoZrZRUqf1YtS70o8t3V81CyXCToF8bAobg0gL1YY4V2tRHCTOrSnjH3J6D6gTm0lLZO9OxjfQe7YU5cSOdxiyLOW0Ag3fPAyD0GQMP80COrldlwCMveUsJyCFmXNjD32HruiDbN20nc9Sq1frXULa4erZ3ZSNnl0DhTvKRfvK7AveoALwNchdIyMgySNFho2d2D3OCaj5UruvcNnR2TJJwVGXIxF0NgFFTBuFfyBfHxKQeYhKMuXwt3qzJx0gXiG3gO6Vj2l6igpnXrTDPllypWvScpge0Yx22D97qGb2m7zLZbWaNLzWoPM79fqWPg7JNlz3sVIxenBTMMMEpWEDRtcNhSeR6As";
        System.out.println(str.hashCode());

    }
}