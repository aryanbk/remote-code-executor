package com.remote.consumer.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Service
public class DockerOutputWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger(DockerOutputWebSocketHandler.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String sessionId = session.getUri().getQuery();
        redisTemplate.opsForValue().set("websocket:session:" + sessionId, session.getId());
        logger.info("New WebSocket connection established for session: {}", sessionId);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String sessionId = session.getUri().getQuery();
        redisTemplate.delete("websocket:session:" + sessionId);
        logger.info("WebSocket connection closed for session: {}", sessionId);
    }

    public void sendMessage(String sessionId, String message) {
        logger.info("Sending message to session: {}", sessionId);
        String storedSessionId = (String) redisTemplate.opsForValue().get("websocket:session:" + sessionId);
        if (storedSessionId != null) {
            WebSocketSession session = getSession(storedSessionId);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                    logger.info("Message sent successfully to session {}", sessionId);
                } catch (IOException e) {
                    logger.error("Error sending message to session {}", sessionId, e);
                }
            } else {
                logger.warn("Session {} is not open", sessionId);
            }
        } else {
            logger.warn("Session {} not found", sessionId);
        }
    }

    private WebSocketSession getSession(String sessionId) {
        // Implement a method to retrieve the WebSocketSession based on the sessionId
        // This could involve maintaining a local cache of sessions or using a
        // distributed cache
        // For simplicity, we'll assume you have a method to retrieve the session
        return null; // Replace with actual implementation
    }

    public void cleanupInactiveSessions() {
        // Implement cleanup logic using Redis if needed
    }
}