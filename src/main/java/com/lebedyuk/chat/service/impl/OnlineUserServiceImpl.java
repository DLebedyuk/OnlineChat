package com.lebedyuk.chat.service.impl;

import com.lebedyuk.chat.model.ChatUser;
import com.lebedyuk.chat.repository.ChatUserRepository;
import com.lebedyuk.chat.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {
    private final ChatUserRepository chatUserRepository;

    @Override
    public void add(String nickname, String sessionId) {
        Optional<ChatUser> chatUser = chatUserRepository.findByNicknameIgnoreCase(nickname);
        if (chatUser.isEmpty()) {
            var newChatUser = new ChatUser();
            newChatUser.setNickname(nickname);
            newChatUser.setSessionId(sessionId);
            chatUserRepository.save(newChatUser);
        } else {
            chatUser.get().setSessionId(sessionId);
            chatUserRepository.save(chatUser.get());
        }
    }

    @Override
    public void delete(String sessionId) {
        var chatUser = chatUserRepository.findBySessionId(sessionId);
        if (chatUser.isPresent()) {
            chatUser.get().setSessionId(null);
            chatUserRepository.save(chatUser.get());
        }
    }

    @Override
    public List<ChatUser> findAll() {
        return chatUserRepository.findBySessionIdNotNull();
    }
}