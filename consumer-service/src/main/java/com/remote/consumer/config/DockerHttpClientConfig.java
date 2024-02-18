package com.remote.consumer.config;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class DockerHttpClientConfig {

    @Bean
    public DockerHttpClient dockerHttpClient(DockerClientConfig config) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    @Bean
    public DockerClient dockerClient(DockerClientConfig config, DockerHttpClient httpClient) {
        return DockerClientImpl.getInstance(config, httpClient);
    }
}
