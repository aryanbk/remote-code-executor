package com.remote.submission.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.remote.submission.model.FileSubmission;
import com.remote.submission.repository.FileRepository;

class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleFileSubmission() {
        FileSubmission submission = new FileSubmission();
        submission.setCodeContent("print('Hello, World!')".getBytes());
        submission.setLanguage("python");
        submission.setUserId(1);

        when(fileRepository.save(any(FileSubmission.class))).thenReturn(submission);

        String result = fileService.handleFileSubmission(submission);

        assertEquals("Success", result);

        verify(fileRepository).save(any(FileSubmission.class));
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(FileSubmission.class));
    }
}