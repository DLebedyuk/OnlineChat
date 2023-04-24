package com.lebedyuk.chat.controller;

import com.lebedyuk.chat.model.Message;
import com.lebedyuk.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/message")
    public Message create(@RequestBody Message message){
        return messageService.create(message);
    }

    @GetMapping("/message")
    public List<Message> getAllMessages(@RequestParam int size){
        var messages = new ArrayList<>(messageService.findAll(size));
        messages.sort(Comparator.comparing(Message::getCreatedAt));
        return messages;
    }

    @MessageMapping("/message")
    @SendTo("/topic/chat")
    public Message receiveAndSend(@Payload Message message) {
        return messageService.create(message);
    }
}
