package com.jung.notify.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String corpCode;

    private String corpName;

    private String dataModifyDate;

    private String stockCode;
}
