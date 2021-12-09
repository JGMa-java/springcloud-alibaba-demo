package com.txlc.user.ctrl;

import com.txlc.common.model.BaseRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试配置中心
 * @author JGMa
 */
@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigTestCtrl {

    @Value("${txlc.config.text}")
    private String text;

    @GetMapping("/getConfig")
    public BaseRes getConfig(){
        return BaseRes.ok(text);
    }

}
