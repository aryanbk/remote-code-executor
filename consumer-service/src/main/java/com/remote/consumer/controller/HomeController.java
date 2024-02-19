package com.remote.consumer.controller;

import com.remote.consumer.service.DockerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    DockerService dockerService;

    @GetMapping("/")
    @ResponseBody
    public String getHomePage(){
        logger.info("hit - consumer home page");
        return "hello! from consumer service";
    }

    @GetMapping("/launch")
    @ResponseBody
    public String getLaunchPage(){
        logger.info("hit - launch page");
        dockerService.launchNginxContainer();
        logger.info("container launched - controller message");
        return "container launched";
    }

    @GetMapping("/list")
    @ResponseBody
    public String getList(){
        logger.info("hit - list page");
        dockerService.listRunningContainers();
        logger.info("launched container - list page");
        return "list";
    }

    @GetMapping("/error")
    @ResponseBody
    public String getError(){
        logger.info("hit - error page");
        return "error 404";
    }
}