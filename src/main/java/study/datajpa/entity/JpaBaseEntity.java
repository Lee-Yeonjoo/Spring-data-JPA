package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass //이게 있어야 속성이 상속된다.
public class JpaBaseEntity {

    @Column(updatable = false) //생성일이 변경되지 않도록. default가 true임
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //JPA가 제공. persist하기 전에 동작
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate //update하기 전에 동작
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
