package com.txlc.provider.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: admin
 */
@RestController
public class ProviderController {
    Logger logger = LoggerFactory.getLogger(ProviderController.class);
    private final String REQUEST_ID = "request_id";

    @GetMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "forezp", required = false) String name) {
        // 还有更加准确的方法
        String s = MDC.get(REQUEST_ID);
        return "hi " + name+"_request_id:"+s;
    }
}
