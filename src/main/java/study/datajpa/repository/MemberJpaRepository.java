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

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList(); //순수jpa에서는 직접 짜야되는데 스프링 데이터 jpa는 쿼리 메소드 기능으로 알아서 만든다.
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class) //네임드 쿼리 사용
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                    .setParameter("age", age)
                    .setFirstResult(offset) //몇번째부터 가져오는지
                    .setMaxResults(limit) //몇개를 가져오는지
                    .getResultList();
    }

    public long totalCount(int age) { //total count를 알려주기 위한 메소드
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); //응답값의 개수가 나온다.
    }
}
