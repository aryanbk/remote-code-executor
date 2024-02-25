package com.remote.consumer.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.remote.consumer.config.Constants;
import com.remote.consumer.model.CodeSubmission;

@Service
public class ListenerService {

    @Autowired
    private DockerService dockerService;

    // @Autowired
    // private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LogManager.getLogger(ListenerService.class);

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = Constants.FILE_EXECUTION_QUEUE, concurrency = "1")
    public void receiveFileExecution(CodeSubmission codeSubmission) {
        logger.info("--------------------Code Submission Received--------------------");
        logger.info("ID: {}", codeSubmission.getId());
        logger.info("Session ID: {}", codeSubmission.getSessionId());
        logger.info("Language: {}", codeSubmission.getLanguage());
        logger.info("User ID: {}", codeSubmission.getUserId());
        logger.info("Code Content: {}", new String(codeSubmission.getCodeContent()));
        logger.info("----------------------------------------------------------------");

        if (codeSubmission.getLanguage().toLowerCase().contains("python")) {
            // byte[] containerRespose =
            // dockerService.createPythonContainer(codeSubmission);
            byte[] containerRespose = dockerService.executeCode(codeSubmission);
            logger.info("Container response: {}", new String(containerRespose));

            // // send response to submission service
            // rabbitTemplate.convertAndSend(Constants.CODE_EXECUTION_RESULT_EXCHANGE,
            // Constants.CODE_EXECUTION_RESULT_ROUTING_KEY,
            // new CodeExecutionResult(codeSubmission, containerRespose));
        }
    }
}