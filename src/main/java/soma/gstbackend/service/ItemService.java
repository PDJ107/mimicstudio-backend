package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Item;
import soma.gstbackend.repository.ItemRepository;
import soma.gstbackend.util.ItemMessageForm;
import soma.gstbackend.util.MessageForm;
import soma.gstbackend.util.MessageProcessor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final MessageProcessor messageProcessor;

    public void join(Item item) throws Exception {
        itemRepository.save(item);

        // SQS 메시지 등록
        MessageForm messageForm = new ItemMessageForm(item.getId(), item.getS3Key());
        messageProcessor.send(messageForm);
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
