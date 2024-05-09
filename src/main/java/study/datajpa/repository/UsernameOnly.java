package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly { //인터페이스만 만들면 스프링 데이터 jpa가 구현체를 만들어서 반환해준다.

    @Value("#{target.username + ' ' + target.age}") //오픈 프로젝션 -> 조회는 엔티티를 다 가져오고, username이랑 age만 처리. target은 member. username이랑 age를 둘 다 가져와서 문자를 더해서 넣어준다.
    String getUsername();
}
