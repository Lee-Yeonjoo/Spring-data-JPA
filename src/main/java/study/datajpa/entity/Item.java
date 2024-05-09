package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id //@GeneratedValue
    private String id;

    @CreatedDate //persist되기 전에 호출됨.
    private LocalDateTime createdDate; //이거로 새로운 엔티티인지를 구분

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() { //식별자를 @GeneratedValue없이 지정한다면 새로운 엔티티인지를 구별하는 로직을 직접 짜야한다.
        return createdDate == null; //createdDate가 null이면 새로운 객체다.
    }
}
