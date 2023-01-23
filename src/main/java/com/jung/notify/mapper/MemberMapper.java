package com.jung.notify.mapper;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper( MemberMapper.class );

    MemberDto.SelectMember memberToSelectMember(Member member);

    Member selectMemberToMember(MemberDto.SelectMember selectMember);

    List<MemberDto.SelectMember> membersToSelectMembers(List<Member> members);

    MemberDto.UpdateMember memberToUpdateMember(Member member);
}
