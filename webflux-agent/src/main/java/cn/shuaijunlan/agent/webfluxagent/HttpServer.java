package cn.shuaijunlan.agent.webfluxagent;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 14:01 2018/5/7.
 */
//@RestController
public class HttpServer {
    @PostMapping(value = "")
    public Integer invoke(@RequestBody RequestParams requestParams) {
        return requestParams.getParameters().hashCode();
    }
    @PostMapping(value = "test2")
    public Integer getHashCode(@RequestParam("parameters") String parameters){
        return parameters.hashCode();
    }

    @PostMapping(value = "test3")
    public Integer getHashCode3(HttpRequest request){
        HttpRequest re = request;
        return 11;
    }
}