package com.remote.consumer.service;

import com.remote.consumer.config.Constants;
import com.remote.consumer.model.FileSubmission;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @RabbitListener(queues = Constants.FILE_EXECUTION_QUEUE, concurrency = "1")
    public void receiveFileExecution(FileSubmission fileSubmission) {
        System.out.println("Received file execution:");
        System.out.println("ID: " + fileSubmission.getId());
        System.out.println("Language: " + fileSubmission.getLanguage());
        System.out.println("User ID: " + fileSubmission.getUserId());
        System.out.println("Code Content: " + new String(fileSubmission.getCodeContent()));
        System.out.println("-----------------------------");
    }
}
