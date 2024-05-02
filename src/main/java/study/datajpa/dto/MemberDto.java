package study.datajpa.dto;

import lombok.Data;

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
}
