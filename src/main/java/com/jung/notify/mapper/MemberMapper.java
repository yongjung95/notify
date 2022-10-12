package com.jung.notify.mapper;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

    MemberDto.SelectMember memberToSelectMember(Member member);

    Member saveMemberToMember(MemberDto.SaveMember saveMember);
}
