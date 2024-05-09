package study.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername(); //중첩 구조에서는 첫번째는 최적화가 되는데

    TeamInfo getTeam(); //두번째부터 최적화가 안된다. 팀은 엔티티로 불러온다. 조인은 외부(left)조인을 한다.
    interface TeamInfo{
        String getName();
    }
}
