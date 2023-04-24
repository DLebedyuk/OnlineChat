package com.lebedyuk.chat.service;

import com.lebedyuk.chat.model.ChatUser;

import java.util.List;

public interface OnlineUserService {
    void add(String nickname, String sessionId);
    void delete(String sessionId);
    List<ChatUser> findAll();

}
