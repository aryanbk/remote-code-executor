# Remote Code Execution Microservice Application

This microservice-based application allows for remote code execution within Docker containers. It provides a scalable and secure way to execute user-submitted code in isolated environments.

## Features

-   Remote code execution in isolated Docker containers
-   Asynchronous communication between microservices using RabbitMQ
-   Real-time output streaming via WebSockets
-   Service discovery with Eureka
-   API Gateway with JWT authentication
-   Load balancing with Nginx
-   Session management using Redis

## Architecture

The application consists of the following microservices:

1. Submission Service: Handles user code submissions and pushes them to RabbitMQ.
2. Consumer Service: Consumes requests from RabbitMQ, launches Docker containers, executes code, and streams output via WebSockets.
3. Eureka Server: Provides service discovery for the microservices.
4. API Gateway: Routes requests to appropriate services and handles authentication.

## Prerequisites

-   Java 21
-   Maven
-   Docker
-   RabbitMQ
-   Redis
-   Nginx (for load balancing)

## Installation and Setup

1. Clone the repository:

    ```
    git clone https://github.com/yourusername/remote-code-execution.git
    cd remote-code-execution
    ```

2. Build the microservices:

    ```
    mvn clean package -DskipTests
    ```

3. Start the Eureka Server:

    ```
    java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
    ```

4. Start the Submission Service:

    ```
    java -jar submission-service/target/submission-service-0.0.1-SNAPSHOT.jar
    ```

5. Start the Consumer Service:

    ```
    java -jar consumer-service/target/consumer-service-0.0.1-SNAPSHOT.jar
    ```

6. Start the API Gateway:
    ```
    java -jar api-gateway-service/target/api-gateway-service-0.0.1-SNAPSHOT.jar
    ```

## Configuration

### Ports

-   Eureka Server: 8761
-   Submission Service: 8080
-   Consumer Service: 8081
-   API Gateway: 8090

### RabbitMQ Configuration

-   spring.rabbitmq.host=localhost
-   spring.rabbitmq.port=5672
-   spring.rabbitmq.username=guest
-   spring.rabbitmq.password=guest

### Redis Configuration

-   spring.redis.host=localhost
-   spring.redis.port=6379
-   spring.redis.password=password

## Usage

1. Submit code in base64 format through the Submission Service API:
   here is the base64 for "print("hello world")":

    ```
    POST /submission/code
    {
      "codeContent": "cHJpbnQoImhlbGxvIHdvcmxkIik=",
      "language": "python",
      "userId": 1
    }
    ```

2. The Consumer Service will execute the code and stream the output via WebSocket.

3. Connect to the WebSocket endpoint to receive real-time output:
    ```
    ws://localhost:8081/docker-output?sessionId
    ```

## Testing

Run the unit tests for each microservice:

```
mvn test
```
