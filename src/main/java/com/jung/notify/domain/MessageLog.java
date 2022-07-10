package com.jung.notify.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLog {

    @Id
    @GeneratedValue
    private Long uid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MessageLogResultCode messageLogResultCode;

    private String failedMessage;

    private LocalDateTime created;

}
