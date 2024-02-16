package com.remote.submission.service;

import com.remote.submission.config.Constants;
import com.remote.submission.model.FileSubmission;
import com.remote.submission.repository.FileRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String handleFileSubmission(FileSubmission fileSubmission){
        fileRepository.save(fileSubmission);
        rabbitTemplate.convertAndSend(Constants.FILE_EXECUTION_EXCHANGE, Constants.FILE_EXECUTION_ROUTING_KEY, fileSubmission);
        return "Success";
    }
}