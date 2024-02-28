package com.remote.consumer.service;

import com.remote.consumer.model.CodeSubmission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListenerServiceTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private ListenerService listenerService;

    private CodeSubmission codeSubmission;

    @BeforeEach
    void setUp() {
        codeSubmission = new CodeSubmission();
        codeSubmission.setId("test-id");
        codeSubmission.setSessionId("test-session");
        codeSubmission.setLanguage("python");
        codeSubmission.setCodeContent("print('Hello, World!')".getBytes());
        codeSubmission.setUserId(1);
    }

    @Test
    void testReceiveFileExecution() {
        when(dockerService.executeCode(any(CodeSubmission.class))).thenReturn("Hello, World!".getBytes());

        listenerService.receiveFileExecution(codeSubmission);

        verify(dockerService, times(1)).executeCode(codeSubmission);
    }

    @Test
    void testReceiveFileExecutionNonPythonLanguage() {
        codeSubmission.setLanguage("java");

        listenerService.receiveFileExecution(codeSubmission);

        verify(dockerService, never()).executeCode(any(CodeSubmission.class));
    }
}