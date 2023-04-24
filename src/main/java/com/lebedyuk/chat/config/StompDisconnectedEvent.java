package com.lebedyuk.chat.config;

import com.lebedyuk.chat.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;



@RequiredArgsConstructor
@Slf4j
@Component
public class StompDisconnectedEvent implements ApplicationListener<SessionDisconnectEvent> {
    private final OnlineUserService onlineUserService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        onlineUserService.delete(event.getSessionId());
        log.info("Client disconnected.");
        log.info(event.toString());
    }
}