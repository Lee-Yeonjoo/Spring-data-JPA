package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter //setter는 없는게 좋다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) //toString 만들어줌 -> team 적으면 안된다 무한 루프가 됨 가급적 연관관계 필드는 toString에서 빼는게 좋다.
@NamedQuery(
        name="Member.findByUsername", //이름은 아무거나 가능. 관례상 엔티티명.메소드명
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team")) //네임드 쿼리처럼 엔티티 그래프에 이름을 붙인다. jpa 표준 스펙
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") //fk명
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        this.team = team;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {  //연관관계 편의 메서드
        this.team = team;
        team.getMembers().add(this); //반대쪽꺼도 변경해준다.
    }
}
