package com.txlc.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-center")
public interface UserCenterFeignClient {

    @GetMapping("/users/name/{name}")
    String usersName(@PathVariable(value = "name", required = false) String name);

}
