package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // em.merge 를 사용하는 것 보다는
    // 따로 수정 코드를 짜서 번거롭더라도 하나하나 값을 바꿔주는 것이 좋다.
    // 이유는 merge를 사용할 시 할당되지 않은 파라미터에 대해서는 null로 대입되기 떄문이다.
    @Transactional     // param이 많을 시 UpdateItemDto 와 같이 DTO를 만들어서 해결하는 방법도 있다.
    public Item updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
//        아래와 같이 setter를 나열하는 것 보다는 이렇게 정의해주는 것이 이후 유지보수, 역추적이 쉽다.
//        findItem.chage(price, name, stockQuantity);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
