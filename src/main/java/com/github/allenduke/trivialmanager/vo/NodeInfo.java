package com.github.allenduke.trivialmanager.vo;

import java.io.Serializable;

/**
 * @author 杜科
 * @description 一个节点的信息
 * @contact AllenDuke@163.com
 * @date 2020/5/1
 */
public class NodeInfo implements Serializable {

    //ip+port
    public String addr;

    public String open;

    public NodeInfo(){}

    public NodeInfo(String addr,String open){
        this.addr=addr;
        this.open=open;
    }
}
