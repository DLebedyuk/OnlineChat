package com.lebedyuk.chat.service.impl;

import com.lebedyuk.chat.model.Message;
import com.lebedyuk.chat.repository.MessageRepository;
import com.lebedyuk.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Override
    public Message create(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll(int size) {
        Pageable sortedByCreatedAt =
                PageRequest.of(0, size, Sort.by("createdAt").descending());
        Page<Message> page = messageRepository.findAll(sortedByCreatedAt);
        return page.getContent();
    }
}
