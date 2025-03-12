package com.demo.sockjs.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String type;  // "public", "private", "group"
    private String sender;
    private String recipient; // For private messages
    private String group;  // For group messages
    private String content;

}
