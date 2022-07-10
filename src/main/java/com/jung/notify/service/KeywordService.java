package com.jung.notify.service;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }

    public Optional<Keyword> findOne(Long id, Member member){
        return keywordRepository.findOne(id, member);
    }

    public Optional<Keyword> findOneByKeyword(String keyword, Member member) {
        return keywordRepository.findOneByKeyword(keyword, member);
    }

    public List<Keyword> findAll(){
        return keywordRepository.findAll();
    }

    public List<Keyword> findAllByMember(Member member){
        return keywordRepository.findAllByMember(member);
    }

    @Transactional
    public void delete(Keyword keyword){
        keywordRepository.delete(keyword);
    }
}
