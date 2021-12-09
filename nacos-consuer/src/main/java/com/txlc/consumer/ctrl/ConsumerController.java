package com.txlc.consumer.ctrl;

import com.txlc.consumer.service.ProviderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author JGMa
 */
@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProviderFeignClient providerFeignClient;

    // restTemplate方式调用服务
    @GetMapping("/hi-restTemplate")
    public String hiResttemplate() {
        return restTemplate.getForObject("http://nacos-provider/hi?name=resttemplate", String.class);

    }

    @GetMapping("/hi-feign")
    public String hiFeign(){
        return providerFeignClient.hi("通过feignd调用");
    }
}
