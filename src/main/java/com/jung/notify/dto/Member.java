package com.jung.notify.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    private String lineToken;

    private LocalDateTime created;
}
