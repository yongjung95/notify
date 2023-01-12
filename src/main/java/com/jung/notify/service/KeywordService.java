package com.jung.notify.service;

import com.jung.notify.common.StringUtil;
import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.News;
import com.jung.notify.dto.KeywordDto;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.KeywordMapper;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;

    private final MemberService memberService;

    private final NewsService newsService;

    private final MessageService messageService;

    public KeywordDto.SelectKeyword saveKeyword(KeywordDto.SaveKeywordDto saveKeywordDto) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(saveKeywordDto.getMemberId()).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        Optional<KeywordDto.SelectKeyword> findKeyword = findOneByKeyword(saveKeywordDto.getKeyword(), saveKeywordDto.getMemberId());

        if (findKeyword.isPresent()) {
            return null;
        }

        Keyword keyword = Keyword.builder()
                .keyword(saveKeywordDto.getKeyword())
                .member(MemberMapper.INSTANCE.selectMemberToMember(selectMember))
                .build();

        keywordRepository.save(keyword);

        return KeywordMapper.INSTANCE.keywordToSelectKeyword(keyword);
    }

    public Optional<KeywordDto.SelectKeyword> findOne(Long id, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return Optional.ofNullable(KeywordMapper.INSTANCE.keywordToSelectKeyword(keywordRepository.findOne(id, MemberMapper.INSTANCE.selectMemberToMember(selectMember)).orElse(null)));
    }

    public Optional<KeywordDto.SelectKeyword> findOneByKeyword(String keyword, String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return Optional.ofNullable(KeywordMapper.INSTANCE.keywordToSelectKeyword(keywordRepository.findOneByKeyword(keyword, MemberMapper.INSTANCE.selectMemberToMember(selectMember)).orElse(null)));
    }

    public List<KeywordDto.SelectKeyword> findAllByMember(String memberId) {
        MemberDto.SelectMember selectMember = memberService.findMemberById(memberId).orElseThrow(NullPointerException::new); // 커스텀 Exception 을 터뜨리면 될 듯.

        return KeywordMapper.INSTANCE.keywordsToSelectKeywords(keywordRepository.findAllByMember(MemberMapper.INSTANCE.selectMemberToMember(selectMember)));
    }

    public void delete(KeywordDto.SelectKeyword selectKeyword) {
        Keyword keyword = keywordRepository.findById(selectKeyword.getId());

        keywordRepository.delete(keyword);
    }

    public void sendKeywordList() {
        List<MemberDto.SelectMember> memberList = memberService.findAllMember();

        for(MemberDto.SelectMember selectMember : memberList){
            if(!StringUtil.isNullOrEmpty(selectMember.getLineToken()) && selectMember.getId().equals("yongjung95")){
                List<KeywordDto.SelectKeyword> keywordList = findAllByMember(selectMember.getId());

                for(KeywordDto.SelectKeyword keyword : keywordList){
                    List<News> newsList = new ArrayList<>();
                    int start = 0;

                    while (newsList.size() < 10 && start < 1000){
                        List<News> resultList = newsService.dateNews(keyword.getKeyword(), start += 10);

                        for (News news : resultList) {
                            if(news.getTitle().contains(keyword.getKeyword()) && newsList.size() < 10){
                                newsList.add(news);
                            }
                        }
                    }

                    if(newsList.size() < 10){
                        List<News> resultList = newsService.news(keyword.getKeyword());

                        for (News news : resultList) {
                            if(newsList.size() < 10){
                                newsList.add(news);
                            }
                        }
                    }

                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("오늘의 " +"\""+keyword.getKeyword() +"\""  + " 뉴스 \n\n");

                    for (News news : newsList){
                        stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
                    }

                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("message", stringBuffer.toString());

                    messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
                }
            }
        }
    }
}
