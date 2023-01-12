package com.jung.notify.service;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.KeywordDto;
import com.jung.notify.mapper.KeywordMapper;
import com.jung.notify.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;

    private final MemberService memberService;

    public KeywordDto.SelectKeyword saveKeyword(KeywordDto.SaveKeywordDto saveKeywordDto) {
        Member member = memberService.findMemberById(saveKeywordDto.getMemberId()).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        Optional<KeywordDto.SelectKeyword> findKeyword = findOneByKeyword(saveKeywordDto.getKeyword(), saveKeywordDto.getMemberId());

        if (findKeyword.isPresent()) {
            return null;
        }

        Keyword keyword = Keyword.builder()
                .keyword(saveKeywordDto.getKeyword())
                .member(member)
                .build();

        keywordRepository.save(keyword);

        return KeywordMapper.INSTANCE.keywordToSelectKeyword(keyword);
    }

    public Optional<KeywordDto.SelectKeyword> findOne(Long id, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return Optional.ofNullable(KeywordMapper.INSTANCE.keywordToSelectKeyword(keywordRepository.findOne(id, member).orElse(null)));
    }

    public Optional<KeywordDto.SelectKeyword> findOneByKeyword(String keyword, String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return Optional.ofNullable(KeywordMapper.INSTANCE.keywordToSelectKeyword(keywordRepository.findOneByKeyword(keyword, member).orElse(null)));
    }

    public List<KeywordDto.SelectKeyword> findAllByMember(String memberId) {
        Member member = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return KeywordMapper.INSTANCE.keywordsToSelectKeywords(keywordRepository.findAllByMember(member));
    }

    public void delete(KeywordDto.SelectKeyword selectKeyword) {
        Keyword keyword = keywordRepository.findById(selectKeyword.getId());

        keywordRepository.delete(keyword);
    }
}
