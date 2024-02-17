package com.remote.consumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class HomeController {

    @GetMapping
    @ResponseBody
    public String getHomePage(){
        System.out.println("consumer home page - hit");
        return "hello from consumer service";
    }
}
