package com.lebedyuk.chat.repository;

import com.lebedyuk.chat.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
    Optional<ChatUser> findByNicknameIgnoreCase(String nickname);
    List<ChatUser> findBySessionIdNotNull();

    Optional<ChatUser> findBySessionId(String sessionId);
}