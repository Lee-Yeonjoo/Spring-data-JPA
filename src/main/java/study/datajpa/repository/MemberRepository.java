package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //List<Member> findByUsername(String username); //쿼리 메소드 기능. 스프링 데이터 jpa가 알아서 만들어준다.

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy(); //By뒤에 없으면 멤버 전체 조회. Hello는 설명글

    //@Query(name = "Member.findByUsername")  //네임드 쿼리를 더 쉽게 사용가능. 네임드 쿼리랑 이름이 같으면 생략 가능. 네임드 쿼리가 없으면 메소드 이름으로 쿼리 생성한다.
    List<Member> findByUsername(@Param("username") String username); //param 어노테이션 필요

    @Query("select m from Member m where m.username = :username and m.age = :age") //실무에서 많이 쓰인다. 리포지토리에 바로 정의. JPQL에서 오타가 나면 애플리케이션 로딩 시점에 에러 발생한다는 큰 장점이 있다.
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
