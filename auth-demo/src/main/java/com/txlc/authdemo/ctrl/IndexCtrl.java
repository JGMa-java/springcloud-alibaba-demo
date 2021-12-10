package com.txlc.authdemo.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JGMa
 */
@RestController
public class IndexCtrl {

    @GetMapping("index")
    public String index(){
        return "hello ok!";
    }

}
