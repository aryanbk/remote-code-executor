package com.remote.submission.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remote.submission.config.Constants;
import com.remote.submission.model.CodeSubmission;
import com.remote.submission.repository.CodeSubmissionRepository;

@Service
public class CodeService {

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public CodeSubmission handleCodeSubmission(CodeSubmission codeSubmission) {
        codeSubmission.setId(UUID.randomUUID().toString());
        if (codeSubmission.getSessionId() == null) {
            codeSubmission.setSessionId(UUID.randomUUID().toString());
        }
        codeSubmissionRepository.save(codeSubmission);
        rabbitTemplate.convertAndSend(Constants.FILE_EXECUTION_EXCHANGE, Constants.FILE_EXECUTION_ROUTING_KEY,
                codeSubmission);
        return codeSubmission;
    }
}