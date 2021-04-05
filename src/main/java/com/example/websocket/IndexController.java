package com.example.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/hello")
    public String index(){
        var index = "index";
        String a = "b";
        return index;
    }
}
