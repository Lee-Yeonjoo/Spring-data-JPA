package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Autowired MemberQueryRepository memberQueryRepository; //굳이 사용자 정의 리포지토리를 쓰지 않고, 이렇게 바로 써도 된다.

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member); //save를 정의하지 않았는데 제공됨.

        Member findMember = memberRepository.findById(savedMember.getId()).get(); //옵셔널로 제공 -> get()하면 옵셔널을 깔 수 있다. orElseThrow로 처리해주는 게 좋음.

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //findMember1.setUsername("member!!!!!"); //알아서 업데이트 쿼리가 나가서 db에도 반영된다. dirty checking(변경 감지)


        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);


        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);


        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        } //실제 테스트에선 assert로 해야한다.
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for (MemberDto dto : usernameList) {
            System.out.println("dto = " + dto);
        } //실제 테스트에선 assert로 해야한다.
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA"); //컬렉션 반환
        Member a = memberRepository.findMemberByUsername("AAA"); //단건 반환
        System.out.println("findMember = " + a);
        Optional<Member> aa = memberRepository.findOptionalByUsername("AAA"); //옵셔널은 없는 경우 Optional.empty. 데이터가 있을 수도 없을 수도 있으면 Optional을 쓰자
        //단건 조회에서 두 개 이상이 조회되면 exception 터진다.

        List<Member> result = memberRepository.findListByUsername("dfsfa"); //조회할 게 없으면 그냥 빈 리스트가 조회되니 굳이 if문으로 검사할 필요 없다!
        System.out.println("result = "+result.size());

        Member findMember = memberRepository.findMemberByUsername("dfsdfe"); //단건인 경우 결과가 없으면 null이다.
        System.out.println("findMember = "+findMember ); //null이다. 순수JPA는 NoResultException이 터진다.

    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")); //스프링 data jpa는 페이지 번호가 0부터 시작한다 주의!. sorting은 빼도 됨

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest); //Pageable 인터페이스를 넘기면 된다. PageRequest는 Pageable의 구현체이다.
        //반환타입이 Page인 것을 보고 totalCount쿼리도 알아서 같이 날린다.

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null)); //Dto로 변환할 때 map기능을 사용할 수 있다.

        //then
        List<Member> content = page.getContent(); //페이지에서의 데이터 3개를 꺼내온다.
        long totalElements = page.getTotalElements(); //totalCount를 가져옴.

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = "+ totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); //getNumber()는 페이지 번호를 가져온다.
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue(); //첫번째 페이지가 맞는지
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는지

    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        //jpa의 기본동작 - save 후에 jpql이 나가기 전에 flush해서 db에 반영된다.

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        /*em.flush(); //남아있는 것들이 db에 반영됨
        em.clear(); //벌크연산 이후에는 영속성 컨텍스트를 다 날려야 한다.*/

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0); //벌크연산은 db에 바로 반영하는거라 영속성 컨텍스트에는 아직 member5의 나이가 40살이다. 벌크연산에서의 주의점!
        System.out.println("member5 = "+ member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush(); //영속성 컨텍스트를 db에 반영시킴
        em.clear(); //영속성 컨텍스트를 날려버림
        
        //when //N + 1 문제 -> 1이 Member, N이 Team
        //지연로딩이므로 팀은 select하지 않음. select Member
        //List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = "+ member.getTeam().getClass()); //프록시 객체이다.
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); //db에 반영만 되고, 영속성 컨텍스트가 아직 비워지진 않음
        em.clear(); //영속성 컨텍스트가 다 날라간다. 이후에 무조건 db를 조회해야함.

        //when
        //Member findMember = memberRepository.findById(member1.getId()).get(); //실무에선 get()쓰면 안됨.
        //findMember.setUsername("member2");

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2"); //readOnly기 때문에 스냅샷을 만들지 않아서 변경되지x

        em.flush(); //변경감지가 동작해서 db에 업데이트 쿼리가 나간다.
        //변경감지의 단점 - 원본이 있어야 변경감지를 할 수 있어서 원본이랑 변경된 것, 두 개의 객체를 관리해야해서 메모리가 더 들어간다.
        //변경할 일이 없더라도 변경감지할 준비를 해놓는다. -> 하이버네이트는 100% 조회용으로 최적화할 수 있다. jpa 표준은 제공x -> 힌트 사용해야함.


    }

    @Test
    public void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA")); //두 개의 specification을 조립
        List<Member> result = memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}