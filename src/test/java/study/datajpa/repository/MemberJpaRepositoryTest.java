package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional //스프링프레임워크의 트랜젝셔널로 선택 - 기능이 더 많다.
//@Rollback(false) //롤백 기능 끄기 - 쿼리 확인 가능. db에도 반영된거 확인 가능. - 실무에서 테스트할 땐 롤백
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //같은 트랜잭션 안이면 비교했을 때 true를 보장한다.
    }
}