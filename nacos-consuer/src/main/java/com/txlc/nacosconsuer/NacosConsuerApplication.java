package com.txlc.nacosconsuer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: admin
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsuerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsuerApplication.class, args);
    }

}
