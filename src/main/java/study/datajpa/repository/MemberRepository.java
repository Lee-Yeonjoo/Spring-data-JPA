package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //List<Member> findByUsername(String username); //쿼리 메소드 기능. 스프링 데이터 jpa가 알아서 만들어준다.

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy(); //By뒤에 없으면 멤버 전체 조회. Hello는 설명글

    //@Query(name = "Member.findByUsername")  //네임드 쿼리를 더 쉽게 사용가능. 네임드 쿼리랑 이름이 같으면 생략 가능. 네임드 쿼리가 없으면 메소드 이름으로 쿼리 생성한다.
    List<Member> findByUsername(@Param("username") String username); //param 어노테이션 필요

    @Query("select m from Member m where m.username = :username and m.age = :age") //실무에서 많이 쓰인다. 리포지토리에 바로 정의. JPQL에서 오타가 나면 애플리케이션 로딩 시점에 에러 발생한다는 큰 장점이 있다.
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList(); //단순 값 조회하는 경우

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t") //new 오퍼레이션을 해줘야 한다.
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")  //IN절을 컬렉션 파라미터 바인딩 할 수 있다.
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //반환타입이 컬렉션
    Member findMemberByUsername(String username); //반환타입이 단건

    Optional<Member> findOptionalByUsername(String username); //반환타입이 단건에 Optional감쌈

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m") //카운트 쿼리를 분리해서 성능 향상시킬 수 있다.
    Page<Member> findByAge(int age, Pageable pageable);

    //clearAutomatically = true를 하면 벌크연산 후에 자동으로 clear()를 호출한다.
    @Modifying(clearAutomatically = true) //executeUpdate()를 호출해준다. 이 어노테이션이 꼭 있어야 벌크업 연산 가능
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
