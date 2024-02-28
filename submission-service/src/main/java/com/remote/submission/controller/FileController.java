package com.remote.submission.controller;

import com.remote.submission.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.remote.submission.model.FileSubmission;

@RestController
@RequestMapping("/submission/file")
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/{codeId}")
    public byte[] getFile(@RequestParam int codeId) {
        return new byte[0];
    }

    @PostMapping
    public String saveFile(@RequestBody FileSubmission fileSubmission) {
        return fileService.handleFileSubmission(fileSubmission);
    }
}