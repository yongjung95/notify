package com.jung.notify.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long uid;

    private String id;

    private String passwd;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String lineToken;

    private LocalDateTime created;
}
