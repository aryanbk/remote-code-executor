package com.remote.consumer.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String FILE_EXECUTION_QUEUE = "file-execution-queue";
    public static final String FILE_EXECUTION_EXCHANGE = "file-execution-exchange";
    public static final String FILE_EXECUTION_ROUTING_KEY = "file-execution-routingKey";
    public static final String DOCKER_HOST = "npipe:////./pipe/docker_engine";
    public static final String DOCKER_REGISTRY_URL = "https://gallery.ecr.aws/";
    public static final String DOCKER_REGISTRY_USERNAME = "4ryan";
    public static final String DOCKER_REGISTRY_PASSWORD = "Tmp*Pwd*000";

    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-HH-mm");

        return now.format(formatter);
    }
}
