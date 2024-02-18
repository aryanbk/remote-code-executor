package com.remote.consumer.controller;

import com.remote.consumer.service.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @Autowired
    DockerService dockerService;

    @GetMapping("/")
    @ResponseBody
    public String getHomePage(){
        System.out.println("consumer home page - hit");
        return "hello from consumer service";
    }

    @GetMapping("/launch")
    @ResponseBody
    public String getLaunchPage(){
        System.out.println("launch page - hit");
        dockerService.launchNginxContainer();
        System.out.println("container launched - controller message");
        return "container launched";
    }


    @GetMapping("/list")
    @ResponseBody
    public String getList(){
        System.out.println("list page - hit");
        dockerService.listRunningContainers();
        System.out.println("list page - end");
        return "list";
    }


}
