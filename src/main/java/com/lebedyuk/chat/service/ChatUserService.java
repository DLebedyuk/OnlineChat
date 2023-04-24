package com.lebedyuk.chat.service;

import com.lebedyuk.chat.model.ChatUser;

public interface ChatUserService {
    ChatUser getOrCreate(ChatUser chatUser);
}
