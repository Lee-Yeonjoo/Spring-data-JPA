package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext //엔티티 매니저를 가져옴
    private EntityManager em;

    //등록
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    //삭제
    public void delete(Member member) {
        em.remove(member);
    }

    //모두 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //단건 조회 - Optional
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //null일 수 있다.
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class) //count가 Long타입으로 반환됨.
                .getSingleResult();
    }

    //단건 조회
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
