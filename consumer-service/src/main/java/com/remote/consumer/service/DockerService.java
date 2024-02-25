package com.remote.consumer.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.remote.consumer.model.CodeSubmission;

@Service
public class DockerService {

    private static final Logger logger = LogManager.getLogger(DockerService.class);

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private DockerOutputWebSocketHandler webSocketHandler;

    public byte[] executeCode(CodeSubmission codeSubmission) {
        String containerId = null;
        try {
            containerId = createContainer(codeSubmission);
            return runContainer(containerId, codeSubmission.getSessionId());
        } finally {
            if (containerId != null) {
                removeContainer(containerId);
            }
        }
    }

    private String createContainer(CodeSubmission codeSubmission) {
        String image = getDockerImageForLanguage(codeSubmission.getLanguage());
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withName("code-execution-" + codeSubmission.getSessionId())
                .withHostConfig(new HostConfig()
                        .withMemory(512 * 1024 * 1024L)
                        .withCpuCount(2L))
                .withCmd(getExecutionCommand(codeSubmission))
                .exec();

        return container.getId();
    }

    private byte[] runContainer(String containerId, String sessionId) {
        dockerClient.startContainerCmd(containerId).exec();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // StringBuilder messageBuffer = new StringBuilder();
        try {
            dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(new ResultCallback.Adapter<Frame>() {
                        @Override
                        public void onNext(Frame frame) {
                            byte[] payload = frame.getPayload();
                            String message = new String(payload);
                            sendWebSocketMessage(sessionId, message);

                            // logger.info("Payload: {}", message);
                            // messageBuffer.append(message);
                            // if (messageBuffer.length() > 1000) { // Send message every 1000 characters
                            // sendWebSocketMessage(sessionId, messageBuffer.toString());
                            // messageBuffer.setLength(0);
                            // }

                            try {
                                outputStream.write(payload);
                            } catch (IOException e) {
                                logger.error("Error writing to output stream", e);
                            }
                        }
                    })
                    .awaitCompletion();
        } catch (InterruptedException e) {
            logger.error("Container execution interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            // if (messageBuffer.length() > 0) {
            // sendWebSocketMessage(sessionId, messageBuffer.toString());
            // }
        }

        return outputStream.toByteArray();
    }

    private void sendWebSocketMessage(String sessionId, String message) {
        logger.info("DockerService - Sending WebSocket message: {}", message);
        try {
            webSocketHandler.sendMessage(sessionId, message);
        } catch (Exception e) {
            logger.error("Error sending WebSocket message", e);
        }
    }

    private void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId)
                .withForce(true)
                .exec();
    }

    private String getDockerImageForLanguage(String language) {
        switch (language.toLowerCase()) {
            case "python" -> {
                return "python:3.9";
            }
            case "java" -> {
                return "openjdk:11";
            }
            case "javascript" -> {
                return "node:14";
            }
            default -> throw new UnsupportedOperationException("Unsupported language: " + language);
        }
    }

    private String[] getExecutionCommand(CodeSubmission codeSubmission) {
        switch (codeSubmission.getLanguage().toLowerCase()) {
            case "python" -> {
                return new String[] { "python", "-c", new String(codeSubmission.getCodeContent()) };
            }
            case "java" -> {
                // For Java, we'd need to write the code to a file, compile it, and then run it.
                // This is a simplified version.
                return new String[] { "java", "-e", new String(codeSubmission.getCodeContent()) };
            }
            case "javascript" -> {
                return new String[] { "node", "-e", new String(codeSubmission.getCodeContent()) };
            }
            default -> throw new UnsupportedOperationException("Unsupported language: " + codeSubmission.getLanguage());
        }
    }

    public void startContainer(String containerId) {

        dockerClient.startContainerCmd(containerId).exec();

    }

    public void stopContainer(String containerId) {

        dockerClient.stopContainerCmd(containerId).exec();

    }

}