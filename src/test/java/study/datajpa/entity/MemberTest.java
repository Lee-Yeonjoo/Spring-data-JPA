package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush(); //강제로 db에 쿼리를 날린다.
        em.clear(); //jpa의 영속성 컨텍스트 캐시를 비운다.

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = "+ member);
            System.out.println("-> member.team = "+ member.getTeam()); //지연로딩이라서 Team의 toString에서 진짜 객체를 조회한다.
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //이때 @PrePersist 발생

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //@PrePersist
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.createdDate = "+ findMember.getCreatedDate());
        System.out.println("findMember.updatedDate = "+ findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy = "+ findMember.getCreatedBy());
        System.out.println("findMember.lastModifiedBy = "+ findMember.getLastModifiedBy());

    }
}