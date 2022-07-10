package com.jung.notify.repository;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class KeywordRepository {

    private final EntityManager em;

    public void save(Keyword keyword){
        em.persist(keyword);
    }

    public Optional<Keyword> findOne(Long id, Member member){
        List<Keyword> keywords = em.createQuery("select k from Keyword k where k.id = :id and k.member = :member", Keyword.class).setParameter("id", id).setParameter("member", member).getResultList();

        return keywords.stream().findAny();
    }

    public Optional<Keyword> findOneByKeyword(String keyword, Member member){
        List<Keyword> keywords = em.createQuery("select k from Keyword k where k.keyword = :keyword and k.member =:member", Keyword.class)
                .setParameter("keyword", keyword)
                .setParameter("member", member)
                .getResultList();

        return keywords.stream().findAny();
    }

    public List<Keyword> findAll(){
        return em.createQuery("select k from Keyword k join fetch k.member", Keyword.class).getResultList();
    }

    public List<Keyword> findAllByMember(Member member){
        return em.createQuery("select k from Keyword k join fetch k.member where k.member = :member", Keyword.class).setParameter("member", member).getResultList();
    }

    public void delete(Keyword keyword){
        em.remove(keyword);
    }
}
