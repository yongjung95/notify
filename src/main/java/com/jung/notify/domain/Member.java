package com.jung.notify.domain;

import com.jung.notify.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long uid;

    private String id;

    private String passwd;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String lineToken;

    private String email;

    public void updateMember(MemberDto.UpdateMember updateMember) {
        this.passwd = updateMember.getPasswd();
        this.lineToken = updateMember.getLineToken();
        this.email = updateMember.getEmail();
    }
}
