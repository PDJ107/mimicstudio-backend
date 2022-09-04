package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import soma.gstbackend.entity.Category;
import soma.gstbackend.entity.Item;
import soma.gstbackend.entity.ItemStatus;
import soma.gstbackend.entity.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class ItemRequestDto {
    @NotNull(message = "s3 key 값이 Null 입니다.")
    public final String s3Key;

    @NotNull(message = "Public 아이템 여부가 Null 입니다.")
    public final Boolean isPublic;

    @NotNull(message = "Category ID가 없습니다.")
    public final Long categoryId;

    @NotNull(message = "title이 없습니다.")
    public final String title;

    @NotNull(message = "descript가 없습니다.")
    public final String descript;

    public Item toEntity(Member member, Category category) {
        Item item = Item.createItem(member, category, ItemStatus.enqueue, s3Key, isPublic, title, descript);
        return item;
    }
}
