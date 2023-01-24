package com.jung.notify.repository;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    public Keyword findOne(Long id, Member searchMember){
        return queryFactory
                .select(keyword1)
                .from(keyword1)
                .leftJoin(keyword1.member, member)
                .where(keyword1.id.eq(id)
                        .and(member.eq(searchMember))
                ).fetchOne();
    }

    public Keyword findOneByKeyword(String keyword, Member searchMember){
        return queryFactory.select(keyword1)
                .from(keyword1)
                .where(keyword1.member.eq(searchMember)
                        .and(keyword1.keyword.eq(keyword)))
                .fetchOne();
    }

    public List<Keyword> findAllByMember(Member searchMember){
        return queryFactory
                .select(keyword1)
                .from(keyword1)
                .leftJoin(keyword1.member, member)
                .where(eqSearchMember(searchMember))
                .fetch();
    }

    public Keyword findById(Long keywordId) {
        return queryFactory
                .select(keyword1)
                .from(keyword1)
                .where(keyword1.id.eq(keywordId))
                .fetchOne();
    }

    public BooleanExpression eqSearchMember(Member searchMember) {
        return searchMember == null ? null : member.eq(searchMember);
    }

    public void delete(Keyword keyword){
        em.remove(keyword);
    }
}
