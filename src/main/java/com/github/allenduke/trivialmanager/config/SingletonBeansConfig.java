package com.github.allenduke.trivialmanager.config;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 杜科
 * @description 一些单例
 * @contact AllenDuke@163.com
 * @date 2020/4/24
 */
@Configuration
public class SingletonBeansConfig {

    @Value("${zookeeper.host}")
    String zookeeperHost;

    @Value("${zookeeper.port}")
    String zookeeperPort;

    @Bean
    public ThreadPoolExecutor executor(){
        return new ThreadPoolExecutor(2,4,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        return new ZooKeeper(zookeeperHost+":"+zookeeperPort,1000,(event)->{
            System.out.println("连上了");
        });
    }
}
