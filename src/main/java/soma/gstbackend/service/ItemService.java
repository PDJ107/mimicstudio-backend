package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Item;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.ItemException;
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
        MessageForm messageForm;
        try {
            messageForm = new ItemMessageForm(item.getId(), item.getS3Key());
            messageProcessor.send(messageForm);
        } catch(Exception e) {
            throw new ItemException(ErrorCode.SQS_Transfer_Failed);
        }
    }

    @Transactional(readOnly = true)
    public Item findItem(Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }
        return item;
    }

    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public void removeItem(Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }
        itemRepository.remove(itemId);
    }
}
