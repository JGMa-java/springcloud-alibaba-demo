package com.txlc.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用nacos-provider的服务
 */
//@FeignClient("nacos-provider")
//public interface ProviderFeignClient {
//    @GetMapping("/hi")
//    String hi(@RequestParam(value = "name", defaultValue = "feignDemo", required = false) String name);
//}
