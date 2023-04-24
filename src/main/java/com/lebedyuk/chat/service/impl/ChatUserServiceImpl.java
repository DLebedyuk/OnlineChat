package com.lebedyuk.chat.service.impl;

import com.lebedyuk.chat.model.ChatUser;
import com.lebedyuk.chat.repository.ChatUserRepository;
import com.lebedyuk.chat.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {
    private final ChatUserRepository chatUserRepository;

    @Override
    public ChatUser getOrCreate(ChatUser chatUser) {
        var existingUser = chatUserRepository.findByNicknameIgnoreCase(chatUser.getNickname());
        return existingUser.orElseGet(() -> chatUserRepository.save(chatUser));
    }
}
