package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "study.datajpa.repository") 스프링부트를 사용하면 생략가능
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() { //등록자, 수정자 저장을 위한 작업. 등록, 수정될 때마다 이 메서드를 호출.
		return () -> Optional.of(UUID.randomUUID().toString()); //랜덤 아이디 생성해서 리턴. 실제로는 스프링 시큐리티로 세션정보를 꺼내서 유저 아이디를 저장.
 	}
}
