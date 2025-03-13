package com.demo.chat.websocket;

import jakarta.annotation.Nullable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserSessionStore userSessionStore;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, UserSessionStore userSessionStore) {
        this.messagingTemplate = messagingTemplate;
        this.userSessionStore = userSessionStore;
    }

    @MessageMapping("/broadcast")
    public void sendPublicMessage(@Payload ChatMessage message, @Header("token") @Nullable String token, Principal principal) {
        if (token == null) {
            messagingTemplate.convertAndSendToUser(principal.getName(), "/direct/private", new ChatMessage("NOT_AUTHORIZED", "System", message.getSender(), "", "You are not authorized"));
            return;
        }
        messagingTemplate.convertAndSend("/broadcast/public", message);
    }

    @MessageMapping("/private")
    public void sendPrivateMessage(@Payload ChatMessage message, @Header("token") @Nullable String token, Principal principal) {
        if (token == null) {
            messagingTemplate.convertAndSendToUser(principal.getName(), "/direct/private", new ChatMessage("NOT_AUTHORIZED", "System", message.getSender(), "", "You are not authorized"));
            return;
        }
        List<String> sessions = userSessionStore.getSessions(message.getRecipient());
        for (String session : sessions) {
            messagingTemplate.convertAndSendToUser(session, "/direct/private", message);
        }
    }
}
