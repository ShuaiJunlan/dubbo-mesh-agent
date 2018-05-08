package cn.shuaijunlan.agent.webfluxagent;

import javafx.beans.binding.IntegerBinding;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 19:25 2018/5/7.
 */
@RestController
public class Demo {
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    @RequestMapping(value = "")
    public Integer invoke(@RequestParam("interface") String interfaceName,
                          @RequestParam("method") String method,
                          @RequestParam("parameterTypesString") String parameterTypesString,
                          @RequestParam("parameter") String parameter) throws Exception {


        return getValue(parameter);
    }
    public Integer getValue(String parameter){
        WebClient client = WebClient.create("http://localhost:3000" + (atomicInteger.getAndIncrement() % 3));
        Mono<Integer> re = client.get().uri("/" + parameter).retrieve().bodyToMono(Integer.class);
        return re.block();
    }
}
