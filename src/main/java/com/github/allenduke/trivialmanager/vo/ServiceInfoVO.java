package com.github.allenduke.trivialmanager.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author 杜科
 * @description 一个服务的信息
 * @contact AllenDuke@163.com
 * @date 2020/4/28
 */
public class ServiceInfoVO implements Serializable {

    //要改为public，否则异常
    public String serviceName;//服务名和曾

    public List<NodeInfo> providers;//服务提供者的信息

    //consumer

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<NodeInfo> getProviders() {
        return providers;
    }

    public void setProviders(List<NodeInfo> providers) {
        this.providers = providers;
    }


}
