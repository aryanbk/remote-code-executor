package com.remote.submission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping("/submission/hello")
    @ResponseBody
    public String hello() {
        return "Hello !";
    }
}
