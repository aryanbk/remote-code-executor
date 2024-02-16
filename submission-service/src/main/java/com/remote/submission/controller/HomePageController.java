package com.remote.submission.controller;

import com.remote.submission.model.CodeSubmissionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomePageController {

    @GetMapping
    @ResponseBody
    public String getHome(){
        return "hi !";
    }


    @PostMapping
    public void postHome(@RequestBody CodeSubmissionRequest codeSubmissionRequest){
        System.out.println(codeSubmissionRequest.getCode());
        return;
    }
}
