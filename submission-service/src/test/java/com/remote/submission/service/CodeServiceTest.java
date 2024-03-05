package com.remote.submission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.remote.submission.model.CodeSubmission;
import com.remote.submission.repository.CodeSubmissionRepository;

class CodeServiceTest {

    @Mock
    private CodeSubmissionRepository codeSubmissionRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CodeService codeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCodeSubmission() {
        CodeSubmission submission = new CodeSubmission();
        submission.setCodeContent("print('Hello, World!')".getBytes());
        submission.setLanguage("python");
        submission.setUserId(1);

        when(codeSubmissionRepository.save(any(CodeSubmission.class))).thenReturn(submission);

        CodeSubmission result = codeService.handleCodeSubmission(submission);

        assertNotNull(result.getId());
        assertNotNull(result.getSessionId());
        assertEquals("python", result.getLanguage());
        assertEquals(1, result.getUserId());

        verify(codeSubmissionRepository).save(any(CodeSubmission.class));
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeSubmission.class));
    }

    @Test
    void testHandleCodeSubmissionWithExistingSessionId() {
        CodeSubmission submission = new CodeSubmission();
        submission.setCodeContent("console.log('Hello, World!');".getBytes());
        submission.setLanguage("javascript");
        submission.setUserId(2);
        submission.setSessionId("existing-session-id");

        when(codeSubmissionRepository.save(any(CodeSubmission.class))).thenReturn(submission);

        CodeSubmission result = codeService.handleCodeSubmission(submission);

        assertNotNull(result.getId());
        assertEquals("existing-session-id", result.getSessionId());
        assertEquals("javascript", result.getLanguage());
        assertEquals(2, result.getUserId());

        verify(codeSubmissionRepository).save(any(CodeSubmission.class));
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CodeSubmission.class));
    }
}