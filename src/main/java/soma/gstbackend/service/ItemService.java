package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Item;
import soma.gstbackend.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public void join(Item item) {
        // 카테고리 예외처리
        //

        // SQS 메시지 등록
        //

        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Item findItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public void removeItem(Long itemId) {
        itemRepository.remove(itemId);
    }
}
