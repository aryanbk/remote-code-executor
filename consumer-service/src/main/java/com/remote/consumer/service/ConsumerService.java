package com.remote.consumer.service;

import com.remote.consumer.config.Constants;
import com.remote.consumer.model.FileSubmission;
// import com.remote.consumer.model.CodeExecutionResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ConsumerService {

    // private DockerClient dockerClient;

    // @PostConstruct
    // public void init() {
    //     this.dockerClient = DockerClientBuilder.getInstance().build();
    // }

    @RabbitListener(queues = Constants.FILE_EXECUTION_QUEUE, concurrency = "1")
    public void receiveFileExecution(FileSubmission fileSubmission) {
        System.out.println("Received file execution:");
        System.out.println("ID: " + fileSubmission.getId());
        System.out.println("Language: " + fileSubmission.getLanguage());
        System.out.println("User ID: " + fileSubmission.getUserId());
        System.out.println("Code Content: " + new String(fileSubmission.getCodeContent()));

        // Execute code
        // CodeExecutionResult result = executeCode(fileSubmission);

        // Store result
        // storeResult(result);

        // Return result to user (implement this based on your architecture)
        // returnResultToUser(result);
    }

    // private CodeExecutionResult executeCode(FileSubmission fileSubmission) {
    //     String language = fileSubmission.getLanguage();
    //     String code = new String(fileSubmission.getCodeContent());
        
    //     try {
    //         // Create a temporary file with the code
    //         Path codePath = Files.createTempFile("code", "." + language);
    //         Files.write(codePath, code.getBytes());

    //         // Create container
    //         CreateContainerResponse container = dockerClient.createContainerCmd("openjdk:11")
    //             .withHostConfig(HostConfig.newHostConfig()
    //                 .withBinds(new Bind(codePath.toString(), new Volume("/code"))))
    //             .withCmd("java", "/code/" + codePath.getFileName().toString())
    //             .exec();

    //         // Start container
    //         dockerClient.startContainerCmd(container.getId()).exec();

    //         // Get container output
    //         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //         dockerClient.logContainerCmd(container.getId())
    //             .withStdOut(true)
    //             .withStdErr(true)
    //             .withFollowStream(true)
    //             .exec(new ExecStartResultCallback(outputStream, System.err))
    //             .awaitCompletion();

    //         // Stop and remove container
    //         dockerClient.stopContainerCmd(container.getId()).exec();
    //         dockerClient.removeContainerCmd(container.getId()).exec();

    //         // Delete temporary file
    //         Files.delete(codePath);

    //         String output = outputStream.toString();
    //         long executionTime = 0; // You need to implement timing logic

    //         return new CodeExecutionResult(fileSubmission.getId(), output, executionTime);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new CodeExecutionResult(fileSubmission.getId(), "Error: " + e.getMessage(), 0);
    //     }
    // }

    // private void storeResult(CodeExecutionResult result) {
    //     // TODO: Implement database storage and Redis caching
    //     System.out.println("Storing result: " + result);
    // }

    // private void returnResultToUser(CodeExecutionResult result) {
    //     // TODO: Implement result return mechanism
    //     System.out.println("Returning result to user: " + result);
    // }
}