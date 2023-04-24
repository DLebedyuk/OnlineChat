package com.lebedyuk.chat.config;

import com.lebedyuk.chat.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class StompConnectedEvent implements ApplicationListener<SessionConnectedEvent> {

    private final OnlineUserService onlineUserService;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        var sessionId = accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
        var nickname = getNickname(accessor);
        if (nickname.isPresent()) {
            onlineUserService.add(nickname.get(), sessionId);
        }

        log.info("Client connected.");
        log.info(event.toString());
    }

    private Optional<String> getNickname(StompHeaderAccessor accessor) {
        try {
            var messageHeaders = (GenericMessage<?>)accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
            var nativeHeaders = (Map<?,?>)messageHeaders.getHeaders().get("nativeHeaders");
            var nickname = (List<?>)nativeHeaders.get("nickname");
            return Optional.of(nickname.get(0).toString());
        }
        catch (RuntimeException e) {
            log.error("Nickname not found", e);
            return Optional.empty();
        }
    }
}
