package com.demo.chat.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String type;
    private String sender;
    private String recipient;
    private String group;
    private String content;

}
