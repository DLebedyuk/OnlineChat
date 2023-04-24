package com.lebedyuk.chat.service;

import com.lebedyuk.chat.model.Message;

import java.util.List;


public interface MessageService {
    Message create(Message message);
    List<Message> findAll(int size);
}
