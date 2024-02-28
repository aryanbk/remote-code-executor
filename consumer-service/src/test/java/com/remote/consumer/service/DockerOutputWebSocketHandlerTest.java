package com.remote.consumer.service;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

class DockerOutputWebSocketHandlerTest {

    private DockerOutputWebSocketHandler webSocketHandler;
    private WebSocketSession session;

    @BeforeEach
    void setUp() {
        webSocketHandler = new DockerOutputWebSocketHandler();
        session = mock(WebSocketSession.class);
    }

    @Test
    void testAfterConnectionEstablished() throws Exception {
        when(session.getUri()).thenReturn(new URI("ws://localhost:8080/docker-output?test-session"));

        webSocketHandler.afterConnectionEstablished(session);

        verify(session, times(1)).getUri();
    }

    @Test
    void testAfterConnectionClosed() throws Exception {
        when(session.getUri()).thenReturn(new URI("ws://localhost:8080/docker-output?test-session"));

        webSocketHandler.afterConnectionEstablished(session);
        webSocketHandler.afterConnectionClosed(session, null);

        verify(session, times(2)).getUri();
    }

    @Test
    void testSendMessage() throws Exception {
        when(session.getUri()).thenReturn(new URI("ws://localhost:8080/docker-output?test-session"));
        when(session.isOpen()).thenReturn(true);

        webSocketHandler.afterConnectionEstablished(session);
        webSocketHandler.sendMessage("test-session", "Test message");

        verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void testSendMessageSessionClosed() throws Exception {
        when(session.getUri()).thenReturn(new URI("ws://localhost:8080/docker-output?test-session"));
        when(session.isOpen()).thenReturn(false);

        webSocketHandler.afterConnectionEstablished(session);
        webSocketHandler.sendMessage("test-session", "Test message");

        verify(session, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    void testSendMessageSessionNotFound() throws Exception {
        webSocketHandler.sendMessage("non-existent-session", "Test message");

        verify(session, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    void testCleanupInactiveSessions() throws Exception {
        when(session.getUri()).thenReturn(new URI("ws://localhost:8080/docker-output?test-session"));
        when(session.isOpen()).thenReturn(false);

        webSocketHandler.afterConnectionEstablished(session);
        webSocketHandler.cleanupInactiveSessions();

        verify(session, times(1)).isOpen();
    }
}