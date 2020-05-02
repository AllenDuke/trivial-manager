package com.github.allenduke.trivialmanager.controller;

import com.github.allenduke.trivialmanager.vo.NodeInfo;
import com.github.allenduke.trivialmanager.vo.ServiceInfoVO;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杜科
 * @description 服务降级Controller
 * @contact AllenDuke@163.com
 * @date 2020/4/28
 */
@RestController
public class LockDownController {

    @Autowired
    ZooKeeper zooKeeper;

    /**
     * @description: 返回所有服务当前信息
     * @param
     * @return: void
     * @author: 杜科
     * @date: 2020/4/28
     */
    @GetMapping("/getAllServiceState")
    public List<ServiceInfoVO> getAllServiceState() throws Exception {
        //拿到所有的服务名
        List<String> serviceNames = zooKeeper.getChildren("/trivial", null);
        List<ServiceInfoVO> serviceInfoVOS=new ArrayList<>(serviceNames.size());
        for (String serviceName : serviceNames) {
            List<String> addrs = zooKeeper.getChildren("/trivial/" + serviceName + "/providers", null);
            ServiceInfoVO serviceInfoVO = new ServiceInfoVO();
            serviceInfoVO.setServiceName(serviceName);
            List <NodeInfo> providers=new ArrayList<>(addrs.size());
            //找出每个服务的所有提供者
            for (String addr : addrs) {
                System.out.println(addr);
                byte[] data = zooKeeper.getData("/trivial/" + serviceName + "/providers/" + addr, null, null);
                String s = new String(data);
                String versionS=s.substring(0,s.indexOf(","));
                s=s.substring(s.indexOf(",")+1);
                String open=s;
                NodeInfo nodeInfo=new NodeInfo(addr,open);
                providers.add(nodeInfo);
            }
            serviceInfoVO.setProviders(providers);
            serviceInfoVOS.add(serviceInfoVO);
        }
        return serviceInfoVOS;
    }

    /**
     * @description: 根据要关闭的服务名，操作zookeeper客户端，改变节点信息open为false
     * @param serviceName 要关闭的服务名
     * @param addr 要关闭服务的地址
     * @return: void
     * @author: 杜科
     * @date: 2020/4/28
     */
    @PostMapping("/serviceDown")
    public void serviceDown(String serviceName,String addr) throws Exception {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/trivial/" + serviceName + "/providers/" + addr, null, stat);
        String s = new String(data);
        String versionS=s.substring(0,s.indexOf(","));
        String open=s.substring(s.indexOf(",")+1);
        if(open.equals("true")) {//如果为true，那就改为false
            byte[] bytes = (versionS + ",false").getBytes();
            zooKeeper.setData("/trivial/" + serviceName + "/providers/" + addr,bytes,stat.getVersion());
        }
    }

    /**
     * @description: 根据要开启的服务名，操作zookeeper客户端，改变节点信息open为true
     * @param serviceName 要关闭的服务名
     * @param addr 要开启服务的地址
     * @return: void
     * @author: 杜科
     * @date: 2020/4/28
     */
    @PostMapping("/serviceOn")
    public void serviceOn(String serviceName,String addr) throws Exception {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/trivial/" + serviceName + "/providers/" + addr, null, stat);
        String s = new String(data);
        String versionS=s.substring(0,s.indexOf(","));
        String open=s.substring(s.indexOf(",")+1);
        if(open.equals("false")) {//如果为false，那就改为true
            byte[] bytes = (versionS + ",true").getBytes();
            zooKeeper.setData("/trivial/" + serviceName + "/providers/" + addr,bytes,stat.getVersion());
        }
    }

}
