package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter //setter는 없는게 좋다.
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    //엔티티는 기본생성자가 있어야 한다. private은 안됨. protected까지만 가능.
    protected Member() { //아무데서나 호출되지 않기 위해 protected
    }

    public Member(String username) {
        this.username = username;
    }

}
