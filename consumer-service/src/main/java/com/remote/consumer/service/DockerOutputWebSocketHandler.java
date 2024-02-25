package com.remote.consumer.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class DockerOutputWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(DockerOutputWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String sessionId = session.getUri().getQuery(); // Assuming the session ID is passed as a query parameter
        sessions.put(sessionId, session);
        logger.info("New WebSocket connection established for session: {}", sessionId);
        printAllSessions();
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String sessionId = session.getUri().getQuery();
        sessions.remove(sessionId);
        logger.info("WebSocket connection closed for session: {}", sessionId);
        printAllSessions();
    }

    public void sendMessage(String sessionId, String message) {
        logger.info("Sending message to session: {}", sessionId);
        printAllSessions();
        WebSocketSession session = sessions.get(sessionId);
        if (session != null) {
            if (session.isOpen()) {
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
            printAllSessions();
            logger.warn("Session {} not found", sessionId);
        }
    }

    public void printAllSessions() {
        logger.info("Sessions currently connected: ");
        for (String s : sessions.keySet()) {
            logger.info("Session {} : {} is connected", s, sessions.get(s).getId());
        }
    }

    // public void sendMessage(String sessionId, String message) {
    // WebSocketSession session = sessions.get(sessionId);
    // if (session != null && session.isOpen()) {
    // try {
    // session.sendMessage(new TextMessage(message));
    // } catch (IOException e) {
    // logger.error("Error sending message to session {}", sessionId, e);
    // }
    // } else {
    // logger.warn("Session {} not found or closed", sessionId);
    // }
    // }

    // public void sendBinaryMessage(byte[] message) {
    // sessions.forEach(session -> {
    // if (session.isOpen()) {
    // try {
    // session.sendMessage(new BinaryMessage(message));
    // } catch (IOException e) {
    // logger.error("Error sending binary message to client {}", session.getId(),
    // e);
    // }
    // } else {
    // sessions.remove(session);
    // logger.info("Removed closed session: {}", session.getId());
    // }
    // });
    // }

    public void cleanupInactiveSessions() {
        sessions.entrySet().removeIf(entry -> !entry.getValue().isOpen());
        logger.info("Cleaned up inactive sessions. Active sessions: {}", sessions.size());
    }
}