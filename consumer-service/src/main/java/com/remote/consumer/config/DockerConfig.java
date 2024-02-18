package com.remote.consumer.config;

// import com.github.dockerjava.api.DockerClient;
// import com.github.dockerjava.core.DockerClientBuilder;
// import com.github.dockerjava.core.DockerClientConfig;
// import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class DockerConfig {

//     @Bean
//     public DockerClient dockerClient() {
//         // return DockerClientBuilder.getInstance().build();
//             return DockerClientBuilder.getInstance()
//         .withDockerHttpClient(new ApacheDockerHttpClient.Builder()
//             .dockerHost(DockerClientConfig.getDefaultDockerHost())
//             .build())
//         .build();
//     }
// }



import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerConfig {

    // @Bean
    // public DockerClientConfig dockerClientConfig() {
    //     return DefaultDockerClientConfig.createDefaultConfigBuilder()
    //             .withDockerHost("tcp://localhost:2375") // Docker daemon host for Windows
    //             .build();
    // }


    @Bean
    public DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(Constants.DOCKER_HOST) // Update this with your Docker host
                .withRegistryUsername(Constants.DOCKER_REGISTRY_USERNAME)
                .withRegistryPassword(Constants.DOCKER_REGISTRY_PASSWORD)
                .build();
    }
}

