package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;

public interface DataStore {

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);
}