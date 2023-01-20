package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
//        return em.find(Member.class, uid);

        return queryFactory.selectFrom(member).where(member.uid.eq(uid)).fetchOne();
    }

    public Optional<Member> findById(String id) {
//        List<Member> members = em.createQuery("select m from Member m where m.id = :id", Member.class).setParameter("id", id).getResultList();
        return Optional.ofNullable(queryFactory.select(member).from(member).where(member.id.eq(id)).fetchOne());
    }

    public Optional<Member> findByIdOrEmail(String id, String email) {
        return Optional.ofNullable(queryFactory.select(member).from(member).where(member.id.eq(id).or(member.email.eq(email))).fetchOne());
    }

    public Optional<Member> findByEmailAndNotUid(Long uid, String email) {
        return Optional.ofNullable(queryFactory.select(member).from(member).where(member.email.eq(email).and(member.uid.ne(uid))).fetchOne());
    }

    public List<Member> findAllMember() {
//        return em.createQuery("select m from Member m", Member.class).getResultList();

        return queryFactory.selectFrom(member).fetch();
    }
}
