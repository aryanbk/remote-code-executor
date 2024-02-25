package com.remote.submission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remote.submission.model.CodeSubmission;
import com.remote.submission.service.CodeService;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    CodeService codeService;

    @PostMapping
    public ResponseEntity<CodeSubmission> submitCode(@RequestBody CodeSubmission codeSubmission) {
        CodeSubmission codeSubmissionResult = codeService.handleCodeSubmission(codeSubmission);
        return ResponseEntity.ok(codeSubmissionResult);
    }
}