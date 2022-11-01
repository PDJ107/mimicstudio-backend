package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.domain.ApplyCoin;
import soma.gstbackend.domain.Item;
import soma.gstbackend.domain.ItemSearch;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.ItemException;
import soma.gstbackend.repository.ItemRepository;
import soma.gstbackend.util.MessageProcessor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final MessageProcessor messageProcessor;

    public void join(Item item) throws Exception {
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Item findItem(Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null || itemRepository.isDeleted(itemId)) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }
        return item;
    }

    @Transactional(readOnly = true)
    public Page<Item> findItems(Long member_id, ItemSearch search, Pageable pageable) {
        return itemRepository.findAll(member_id, search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> findPublicItems(ItemSearch search, Pageable pageable) {
        return itemRepository.findPublicAll(search, pageable);
    }

    public void removeItem(Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null || itemRepository.isDeleted(itemId)) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }
        itemRepository.remove(itemId);
    }

    public void applyCoin(ApplyCoin apply) {
        Optional<ApplyCoin> applyFromDB = itemRepository.findApplyByEmail(apply.getEmail());
        if(applyFromDB.isPresent()) {
            applyFromDB.get().update(
                    apply.getUserType(),
                    apply.getPurpose(),
                    apply.getProductDescript(),
                    apply.getProductUrl()
            );
        }
        else {
            itemRepository.saveApply(apply);
        }
    }
}
