package com.remote.consumer.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.remote.consumer.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    @Autowired
    private DockerClient dockerClient;

    public void createAndStartContainer(String imageName) {
        try {
            dockerClient.pullImageCmd(imageName)
                    .exec(new ResultCallback.Adapter<PullResponseItem>() {
                        @Override
                        public void onNext(PullResponseItem item) {
                            System.out.println(item.getStatus());
                        }
                    }).awaitCompletion(60, TimeUnit.SECONDS);

//            dockerClient.pullImageCmd("baeldung/alpine")
//                    .withTag("git")
//                    .exec(new PullImageResultCallback())
//                    .awaitCompletion(30, TimeUnit.SECONDS);

            System.out.println("pulled docker image");

            CreateContainerResponse container = dockerClient.createContainerCmd("nginx")
                    .withName("my-nginx-container")
                    .exec();

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void launchNginxContainerOld() {
        try{
            // Pull the official Nginx image from Docker Hub
            dockerClient.pullImageCmd("nginx/nginx:1.27.1-alpine3.20-perl")
                    .start()
                    .awaitCompletion(60, TimeUnit.SECONDS);

            System.out.println("pulled docker image");
            // Create a container from the pulled Nginx image
            CreateContainerResponse container = dockerClient.createContainerCmd("nginx/nginx:1.27.1-alpine3.20-perl")
                    .withName("my-nginx-container")
                    .exec();
            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();

            System.out.println("Nginx container launched successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchNginxContainer() {
        try {
            // Pull the official Nginx image from Docker Hub
            dockerClient.pullImageCmd("nginx:latest")
                    .start()
                    .awaitCompletion(2, TimeUnit.MINUTES);
            System.out.println("Docker image pulled successfully");
            listImages();

            // Create a container from the pulled Nginx image
            CreateContainerResponse container = dockerClient.createContainerCmd("nginx:latest")
                    .withName("my-nginx-container-"+ Constants.getTime())
                    .withHostConfig(new HostConfig()
                            .withPortBindings(PortBinding.parse("8079:80")))
                    .exec();

            // Start the container
            dockerClient.startContainerCmd(container.getId()).exec();
            System.out.println("Nginx container launched successfully with ID: "+container.getId());

            listRunningContainers();

        } catch (InterruptedException e) {
            System.out.println("Image pull was interrupted " + e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Failed to launch Nginx container " + e);
        }
    }


    public void listImages(){
        List<Image> images = dockerClient.listImagesCmd().exec();
        for (Image image : images) {
            System.out.println(image.getId() + " : " + Arrays.toString(image.getRepoTags()));
        }
    }



    public List<Container> listRunningContainers() {
        // List the running containers
        List<Container> containers = dockerClient.listContainersCmd()
                .withShowAll(false) // Only show running containers
                .exec();

        // Print container details
        for (Container container : containers) {
            System.out.println("Container ID: " + container.getId());
            System.out.println("Image: " + container.getImage());
            System.out.println("Names: " + String.join(", ", container.getNames()));
            System.out.println("Status: " + container.getStatus());
            System.out.println("-------------");
        }

        return containers;
    }

}


