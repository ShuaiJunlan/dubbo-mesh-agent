package cn.shuaijunlan.xagent.transport.support;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * @author Junlan Shuai[shuaijunlan@gmail.com].
 * @date Created on 11:21 2018/4/29.
 */
public class MessageResponse{
    private Long id;

    private Integer hash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
