package com.remote.consumer.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DockerService {

    @Autowired
    private DockerClient dockerClient;

    public void createAndStartContainer(String imageName, String containerName) {
        try {
            // Pull the image
            dockerClient.pullImageCmd(imageName)
                    .exec(new PullImageResultCallback())
                    .awaitCompletion();

            // Create the container
            CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                    .withName(containerName)
                    .withHostConfig(HostConfig.newHostConfig())
                    .exec();

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
