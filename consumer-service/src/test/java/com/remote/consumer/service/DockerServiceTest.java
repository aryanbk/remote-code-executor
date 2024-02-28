package com.remote.consumer.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.remote.consumer.model.CodeSubmission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DockerServiceTest {

    @Mock
    private DockerClient dockerClient;

    @Mock
    private DockerOutputWebSocketHandler webSocketHandler;

    @InjectMocks
    private DockerService dockerService;

    private CodeSubmission codeSubmission;

    @BeforeEach
    void setUp() {
        codeSubmission = new CodeSubmission();
        codeSubmission.setId("test-id");
        codeSubmission.setSessionId("test-session");
        codeSubmission.setLanguage("python");
        codeSubmission.setCodeContent("print('Hello, World!')".getBytes());
    }

    @Test
    void testExecuteCode() throws Exception {
        // Mock Docker client responses
        CreateContainerCmd createContainerCmd = mock(CreateContainerCmd.class);
        CreateContainerResponse createContainerResponse = mock(CreateContainerResponse.class);
        StartContainerCmd startContainerCmd = mock(StartContainerCmd.class);
        LogContainerCmd logContainerCmd = mock(LogContainerCmd.class);

        when(dockerClient.createContainerCmd(any())).thenReturn(createContainerCmd);
        when(createContainerCmd.withName(any())).thenReturn(createContainerCmd);
        when(createContainerCmd.withHostConfig(any())).thenReturn(createContainerCmd);
        when(createContainerCmd.withCmd(any(String[].class))).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenReturn(createContainerResponse);
        when(createContainerResponse.getId()).thenReturn("test-container-id");
        when(dockerClient.startContainerCmd(any())).thenReturn(startContainerCmd);
        when(dockerClient.logContainerCmd(any())).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdOut(true)).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdErr(true)).thenReturn(logContainerCmd);
        when(logContainerCmd.withFollowStream(true)).thenReturn(logContainerCmd);

        // Execute the code
        byte[] result = dockerService.executeCode(codeSubmission);

        // Verify that the container was created, started, and removed
        verify(dockerClient).createContainerCmd(any());
        verify(dockerClient).startContainerCmd(any());
        verify(dockerClient).removeContainerCmd(any());

        // Verify that the WebSocket handler was called to send messages
        verify(webSocketHandler, atLeastOnce()).sendMessage(eq("test-session"), any());

        // Assert that the result is not null
        assertNotNull(result);
    }

    @Test
    void testGetDockerImageForLanguage() {
        assertEquals("python:3.9", dockerService.getDockerImageForLanguage("python"));
        assertEquals("openjdk:11", dockerService.getDockerImageForLanguage("java"));
        assertEquals("node:14", dockerService.getDockerImageForLanguage("javascript"));
        assertThrows(UnsupportedOperationException.class,
                () -> dockerService.getDockerImageForLanguage("unsupported"));
    }

    @Test
    void testGetExecutionCommand() {
        String[] pythonCommand = dockerService.getExecutionCommand(codeSubmission);
        assertArrayEquals(new String[] { "python", "-c", "print('Hello, World!')" }, pythonCommand);

        codeSubmission.setLanguage("java");
        String[] javaCommand = dockerService.getExecutionCommand(codeSubmission);
        assertArrayEquals(new String[] { "java", "-e", "print('Hello, World!')" }, javaCommand);

        codeSubmission.setLanguage("javascript");
        String[] jsCommand = dockerService.getExecutionCommand(codeSubmission);
        assertArrayEquals(new String[] { "node", "-e", "print('Hello, World!')" }, jsCommand);

        codeSubmission.setLanguage("unsupported");
        assertThrows(UnsupportedOperationException.class, () -> dockerService.getExecutionCommand(codeSubmission));
    }
}