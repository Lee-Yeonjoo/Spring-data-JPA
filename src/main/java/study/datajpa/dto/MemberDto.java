package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data //getter, setter가 들어가있어서 엔티티엔 쓰지 말자
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) { //생성자 필요
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) { //엔티티는 dto를 보면 안되지만, dto는 엔티티를 볼 수 있으므로 파라미터로 바로 멤버를 넣어줄 수 있다.
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
