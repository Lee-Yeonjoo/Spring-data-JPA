package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username); //쿼리 메소드 기능. 스프링 데이터 jpa가 알아서 만들어준다.

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy(); //By뒤에 없으면 멤버 전체 조회. Hello는 설명글
}
