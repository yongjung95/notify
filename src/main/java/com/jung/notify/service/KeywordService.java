package com.jung.notify.service;

import com.jung.notify.dto.Keyword;
import com.jung.notify.dto.Member;
import com.jung.notify.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }

    public Keyword findOne(Long id){
        return keywordRepository.findOne(id);
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
