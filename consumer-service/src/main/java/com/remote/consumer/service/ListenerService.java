package com.remote.consumer.service;

import com.remote.consumer.config.Constants;
import com.remote.consumer.model.FileSubmission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    private static final Logger logger = LogManager.getLogger(ListenerService.class);

    @RabbitListener(queues = Constants.FILE_EXECUTION_QUEUE, concurrency = "1")
    public void receiveFileExecution(FileSubmission fileSubmission) {
        logger.info("Received file execution:");
        logger.info("ID: {}", fileSubmission.getId());
        logger.info("Language: {}", fileSubmission.getLanguage());
        logger.info("User ID: {}", fileSubmission.getUserId());
        logger.info("Code Content: {}", new String(fileSubmission.getCodeContent()));
    }
}