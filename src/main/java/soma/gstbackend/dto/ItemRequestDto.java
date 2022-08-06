package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import soma.gstbackend.entity.Category;
import soma.gstbackend.entity.Item;
import soma.gstbackend.entity.ItemStatus;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class ItemRequestDto {
    @NotNull(message = "s3 key 값이 Null 입니다.")
    public final String s3Key;

    @NotNull(message = "Public 아이템 여부가 Null 입니다.")
    public final Boolean isPublic;

    @NotNull(message = "Category ID가 없습니다.")
    public final Long categoryId;

    public Item toEntity(Category category) {
        Item item = new Item(ItemStatus.enqueue, s3Key, isPublic);
        item.setCategory(category);
        return item;
    }
}
