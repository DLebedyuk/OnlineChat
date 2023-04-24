package com.lebedyuk.chat.controller;

import com.lebedyuk.chat.model.ChatUser;
import com.lebedyuk.chat.service.ChatUserService;
import com.lebedyuk.chat.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatUserController {
    private final ChatUserService chatUserService;
    private final OnlineUserService onlineUserService;

    @PostMapping("/chat-user")
    public ChatUser getOrCreate(@RequestBody ChatUser chatUser) {
        return chatUserService.getOrCreate(chatUser);
    }

    @GetMapping("/online-chat-user")
    public List<ChatUser> getOnlineUsers(){
        return onlineUserService.findAll();
    }

}
