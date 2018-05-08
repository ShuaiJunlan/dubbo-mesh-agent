package cn.shuaijunlan.agent.webfluxagent;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 19:25 2018/5/7.
 */
@RestController
public class Demo {
    @RequestMapping(value = "")
    public Integer invoke(@RequestParam("interface") String interfaceName,
                          @RequestParam("method") String method,
                          @RequestParam("parameterTypesString") String parameterTypesString,
                          @RequestParam("parameter") String parameter) throws Exception {
        Thread.sleep(50);
        return provider(interfaceName,method,parameterTypesString,parameter);

    }

    public Integer provider(String interfaceName,String method,String parameterTypesString,String parameter) throws Exception {

        return parameter.hashCode();
    }
}
