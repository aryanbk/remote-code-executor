package com.remote.consumer.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.remote.consumer.service.DockerOutputWebSocketHandler;

@RestController
public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    // @Autowired
    // DockerService dockerService;

    @Autowired
    DockerOutputWebSocketHandler webSocketHandler;

    @GetMapping("/")
    @ResponseBody
    public String getHomePage() {
        logger.info("hit - consumer home page");
        return "hello! from consumer service";
    }

    // @GetMapping("/launch")
    // @ResponseBody
    // public String getLaunchPage() {
    // logger.info("hit - launch page");
    // dockerService.launchNginxContainer();
    // logger.info("container launched - controller message");
    // return "container launched";
    // }

    // @GetMapping("/list")
    // @ResponseBody
    // public String getList() {
    // logger.info("hit - list page");
    // dockerService.listRunningContainers();
    // logger.info("launched container - list page");
    // return "list";
    // }

    @GetMapping("/python")
    @ResponseBody
    public String getPython() {
        logger.info("hit - python page");
        byte[] response = new byte[0];
        // byte[] response =
        // dockerService.createPythonContainer(UUID.randomUUID().toString(),
        // "print('hello')".getBytes());
        String responseString = new String(response);
        logger.info("controller - response from python container: {}", responseString);
        return responseString;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String getHello() {
        logger.info("hit - hello page");
        try {
            webSocketHandler.sendMessage("test1234", "hello");
        } catch (Exception e) {
            logger.error("Error sending WebSocket message", e);
        }
        return "hello";
    }

    @GetMapping("/error")
    @ResponseBody
    public String getError() {
        logger.info("hit - error page");
        return "error 404";
    }
}