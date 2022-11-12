package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.domain.ApplyCoin;
import soma.gstbackend.domain.Item;
import soma.gstbackend.domain.ItemSearch;
import soma.gstbackend.enums.ItemStatus;
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

    public Item patchItem(Long itemId, Item item) {
        Item itemFromDB = itemRepository.findOne(itemId);
        if(itemFromDB == null || itemRepository.isDeleted(itemId)) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }

        if(item.getIsPublic() != null) {
            itemFromDB.setPublic(item.getIsPublic());
        }
        if(item.getTitle() != null) {
            itemFromDB.setTitle(item.getTitle());
        }
        if(item.getDescript() != null) {
            itemFromDB.setDescript(item.getDescript());
        }
        if(item.getType() != null) {
            itemFromDB.setType(item.getType());
        }
        if(item.getStatus() != null) {
            itemFromDB.setStatus(item.getStatus());
        }

        return itemFromDB;
    }

    public void removeItem(Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null || itemRepository.isDeleted(itemId)) {
            throw new ItemException(ErrorCode.Item_Not_Found);
        }
        itemRepository.remove(itemId);
    }

    public void validateItem(Long memberId, Long itemId) throws Exception {
        Item item = itemRepository.findOne(itemId);
        if(item == null) {
            throw new IllegalArgumentException("없는 아이템 입니다.");
        }
        if(!item.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("유효한 memberId, itemId가 아닙니다.");
        }
        if(!item.getStatus().equals(ItemStatus.ready)) {
            throw new IllegalArgumentException("아이템이 ready 상태가 아닙니다.");
        }
    }

    public void enqueue(Long itemId, String s3Key) throws Exception {
        Item item = findItem(itemId);
        item.enqueue(s3Key);
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
