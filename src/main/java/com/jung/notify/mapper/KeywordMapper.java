package com.jung.notify.mapper;

import com.jung.notify.domain.Keyword;
import com.jung.notify.dto.KeywordDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface KeywordMapper {

    KeywordMapper INSTANCE = Mappers.getMapper( KeywordMapper.class );

    @Mapping(source = "member.id", target = "memberId")
    KeywordDto.SelectKeyword keywordToSelectKeyword(Keyword keyword);

    List<KeywordDto.SelectKeyword> keywordsToSelectKeywords(List<Keyword> keywords);
}
