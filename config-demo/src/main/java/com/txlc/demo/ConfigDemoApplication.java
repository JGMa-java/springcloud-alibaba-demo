package com.txlc.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JGMa
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope
public class ConfigDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigDemoApplication.class, args);
    }

    @Value("${txlc.config.text}")
    private String config;

    @RequestMapping("/getValue")
    public String getValue() {
        return config;
    }
}
