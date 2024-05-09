package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        Item item = new Item("A"); //pk를 @GeneratedValue로 안하고, 임의의 값으로 정하면 save의 isNew를 판단할 때 새로운 엔티티로 판단하지 않는다는 문제 발생. -> persist가 아닌 merge가 호출됨
        itemRepository.save(item); //pk가 null이니까 새로운 엔티티이다. generatedValue는 persist후에 작동.
    }
}