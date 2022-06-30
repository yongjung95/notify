package com.jung.notify.repository;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class KeywordRepository {

    private final EntityManager em;

    public void save(Keyword keyword){
        em.persist(keyword);
    }

    public Keyword findOne(Long id){
        return em.find(Keyword.class, id);
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
