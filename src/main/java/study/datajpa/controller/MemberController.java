package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) { //간단한 경우에만 쓸 수 있어서 도메인 클래스 컨버터를 권장하진 않는다. 조회용으로만 쓴다.
        return member.getUsername(); //스프링 data jpa가 멤버를 converting하는 과정을 끝내고 바로 리턴해준다.
    }

    @GetMapping("/members")            //size를 여기서 설정하면 글로벌 설정보다 우선임
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) { //Page와 Pageable은 인터페이스
        Page<Member> page = memberRepository.findAll(pageable); //파라미터로 pageable을 넘겨준다. findBy~ 도 마지막 파라미터에 pageable을 넘겨준다.
        Page<MemberDto> map = page.map(MemberDto::new); //엔티티 대신 dto로 반환하도록! map을 이용해 쉽게 변경가능
        return map;
    }

    @PostConstruct //애플리케이션 실행될 때 실행됨
    public void init() {
        //memberRepository.save(new Member("userA"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
