package com.jung.notify.repository;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.jung.notify.domain.QKeyword.keyword1;
import static com.jung.notify.domain.QMember.member;

@Repository
public class KeywordRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public KeywordRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void save(Keyword keyword){
        em.persist(keyword);
    }

    public Optional<Keyword> findOne(Long id, Member searchMember){
//        List<Keyword> keywords = em.createQuery("select k from Keyword k where k.id = :id and k.member = :member", Keyword.class).setParameter("id", id).setParameter("member", member).getResultList();

        return Optional.ofNullable(queryFactory
                .select(keyword1)
                .from(keyword1)
                .leftJoin(keyword1.member, member)
                .where(keyword1.id.eq(id)
                        .and(member.eq(searchMember))
                ).fetchOne());
    }

    public Optional<Keyword> findOneByKeyword(String keyword, Member member){
        List<Keyword> keywords = em.createQuery("select k from Keyword k where k.keyword = :keyword and k.member =:member", Keyword.class)
                .setParameter("keyword", keyword)
                .setParameter("member", member)
                .getResultList();

        return keywords.stream().findAny();
    }

    public List<Keyword> findAll(){
//        return em.createQuery("select k from Keyword k join fetch k.member", Keyword.class).getResultList();

        return queryFactory
                .selectFrom(keyword1)
                .leftJoin(keyword1.member, member)
                .fetch();
    }

    public List<Keyword> findAllByMember(Member searchMember){
//        return em.createQuery("select k from Keyword k join fetch k.member where k.member = :member", Keyword.class).setParameter("member", member).getResultList();

        return queryFactory
                .select(keyword1)
                .from(keyword1)
                .leftJoin(keyword1.member, member)
                .where(member.eq(searchMember))
                .fetch();
    }

    public void delete(Keyword keyword){
        em.remove(keyword);
    }
}
