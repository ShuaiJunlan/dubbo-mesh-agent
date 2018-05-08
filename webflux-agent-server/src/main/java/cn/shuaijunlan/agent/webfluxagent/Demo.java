package cn.shuaijunlan.agent.webfluxagent;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 19:25 2018/5/7.
 */
@RestController
public class Demo {
    @GetMapping(value = "{parameter}")
    public Mono<Integer> invoke(@PathVariable("parameter") String parameter) throws Exception {
        return Mono.just(parameter.hashCode());
    }
}
