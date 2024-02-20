package com.remote.consumer.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.remote.consumer.config.Constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    private static final Logger logger = LogManager.getLogger(DockerService.class);

    @Autowired
    private DockerClient dockerClient;

    public void createAndStartContainer(String imageName) {
        try {
            dockerClient.pullImageCmd(imageName)
                    .exec(new ResultCallback.Adapter<PullResponseItem>() {
                        @Override
                        public void onNext(PullResponseItem item) {
                            logger.info(item.getStatus());
                        }
                    }).awaitCompletion(60, TimeUnit.SECONDS);

            logger.info("pulled docker image");

            CreateContainerResponse container = dockerClient.createContainerCmd("nginx")
                    .withName("my-nginx-container")
                    .exec();

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
        } catch (InterruptedException e) {
            logger.error("Error creating and starting container", e);
        }
    }

    public void launchNginxContainerOld() {
        try{
            // Pull the official Nginx image from Docker Hub
            dockerClient.pullImageCmd("nginx/nginx:1.27.1-alpine3.20-perl")
                    .start()
                    .awaitCompletion(60, TimeUnit.SECONDS);

            logger.info("pulled docker image");
            // Create a container from the pulled Nginx image
            CreateContainerResponse container = dockerClient.createContainerCmd("nginx/nginx:1.27.1-alpine3.20-perl")
                    .withName("my-nginx-container")
                    .exec();
            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();

            logger.info("Nginx container launched successfully!");
        } catch (Exception e) {
            logger.error("Error launching Nginx container", e);
        }
    }

    public void launchNginxContainer() {
        try {
            // Pull the official Nginx image from Docker Hub
            dockerClient.pullImageCmd("nginx:latest")
                    .start()
                    .awaitCompletion(2, TimeUnit.MINUTES);
            logger.info("Docker image pulled successfully");
            listImages();

            // Create a container from the pulled Nginx image
            CreateContainerResponse container = dockerClient.createContainerCmd("nginx:latest")
                    .withName("my-nginx-container-"+ Constants.getTime())
                    .withHostConfig(new HostConfig()
                            .withPortBindings(PortBinding.parse("8079:80")))
                    .exec();

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
            logger.info("Nginx container launched successfully with ID: {}", container.getId());

            listRunningContainers();

        } catch (InterruptedException e) {
            logger.error("Image pull was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Failed to launch Nginx container", e);
        }
    }

    public byte[] createPythonContainer(String id, byte[] code){
        Path codePath = Path.of("/tmp");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            codePath = Files.createTempFile(id, ".py");
            Files.write(codePath, code);
            
            CreateContainerResponse container = dockerClient.createContainerCmd("python:3.9")
            .withName("python-"+id)
            .withHostConfig(new HostConfig()
                .withMemory(512 * 1024 * 1024L)  // 512MB
                .withCpuCount(2L)
                .withBinds(new Bind(codePath.toAbsolutePath().toString(), new Volume("/code/script.py")))
                .withPortBindings(PortBinding.parse("8091:80")))
            .withCmd("python", "/code/script.py")
            .exec();
            
            logger.info("Python container created successfully with ID: {}", container.getId());
            
            dockerClient.startContainerCmd(container.getId()).exec();
            
            dockerClient.logContainerCmd(container.getId())
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            outputStream.write(frame.getPayload());
                        } catch (IOException e) {
                            logger.error("Error writing to output stream", e);
                        }
                    }
                })
                .awaitCompletion();

            logger.info("Output: {}", outputStream.toString());
        } catch (Exception e) {
            logger.error("Error creating python container", e);
        }

        return outputStream.toByteArray();
    }

    public void startContainer(String containerId){
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainer(String containerId){
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void listImages(){
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (Image image : images) {
            logger.info("{} : {}", image.getId(), Arrays.toString(image.getRepoTags()));
        }
    }

    public List<Container> listRunningContainers() {
        // List the running containers
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowAll(false) // Only show running containers
                .exec();

        // Print container details
        for (Container container : containers) {
            logger.info("Container ID: {}", container.getId());
            logger.info("Image: {}", container.getImage());
            logger.info("Names: {}", String.join(", ", container.getNames()));
            logger.info("Status: {}", container.getStatus());
            logger.info("-------------");
        }

        return containers;
    }
}