package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) { //생성자가 중요. 파라미터 이름으로 매칭해서 프로젝션한다.
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
