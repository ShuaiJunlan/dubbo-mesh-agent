package cn.shuaijunlan.agentdemo;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 10:29 2018/5/2.
 */
@RestController
public class DemoController {
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
//        return (byte[]) result;
    }

}
