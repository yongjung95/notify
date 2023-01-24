package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jung.notify.domain.QMember.member;

@Transactional
@Repository
public class MemberRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    public MemberRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Long save(Member member) {
        em.persist(member);

        return member.getUid();
    }

    public Member findByUid(Long uid) {
        return queryFactory.selectFrom(member).where(member.uid.eq(uid)).fetchOne();
    }

    public Member findById(String id) {
        return queryFactory.select(member).from(member).where(member.id.eq(id)).fetchOne();
    }

    public Member findByIdOrEmail(String id, String email) {
        return queryFactory.select(member).from(member).where(member.id.eq(id).or(member.email.eq(email))).fetchOne();
    }

    public Member findByEmailAndNotUid(String email, Long uid) {
        return queryFactory.select(member).from(member).where(member.email.eq(email).and(member.uid.ne(uid))).fetchOne();
    }

    public Member findMemberByEmail(String email) {
        return queryFactory.select(member).from(member).where(member.email.eq(email)).fetchOne();
    }

    public Member findMemberByIdAndEmail(String id, String email) {
        return queryFactory.select(member).from(member).where(member.id.eq(id).and(member.email.eq(email))).fetchOne();
    }

    public List<Member> findAllMember() {
//        return em.createQuery("select m from Member m", Member.class).getResultList();

        return queryFactory.selectFrom(member).fetch();
    }
}
