package com.demo.chat.websocket;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private final UserSessionStore userSessionStore;

    public WebSocketInterceptor(UserSessionStore userSessionStore) {
        this.userSessionStore = userSessionStore;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String username = accessor.getFirstNativeHeader("user");
                if (username != null) {
                    String sessionId = userSessionStore.connect(username);
                    accessor.setUser(() -> sessionId);
                }
            }

            if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                Principal principal = accessor.getUser();
                userSessionStore.disconnect(principal != null ? principal.getName() : null);
            }
        }
        return message;
    }
}
