package com.remote.submission.service;

import com.remote.submission.config.Constants;
import com.remote.submission.model.FileSubmission;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public String sendCodeExecution(FileSubmission fileSubmission) {
        rabbitTemplate.convertAndSend(Constants.FILE_EXECUTION_QUEUE, Constants.FILE_EXECUTION_ROUTING_KEY, fileSubmission);
        return "Success";
    }
}