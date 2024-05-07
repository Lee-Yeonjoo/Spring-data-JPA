package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}
