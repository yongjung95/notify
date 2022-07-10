package com.jung.notify.repository;

import com.jung.notify.domain.MessageLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final EntityManager em;

    public void createMessageLog(MessageLog messageLog){
        em.persist(messageLog);
    }
}
