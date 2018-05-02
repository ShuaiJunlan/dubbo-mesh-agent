package cn.shuaijunlan.xagent.transport.support;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:20 2018/4/29.
 */
public class MessageRequest {
    private Long id;
    private String interfaceName;
    private String method;
    private String parameterTypesString;
    private String parameter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameterTypesString() {
        return parameterTypesString;
    }

    public void setParameterTypesString(String parameterTypesString) {
        this.parameterTypesString = parameterTypesString;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
