package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import soma.gstbackend.domain.Category;
import soma.gstbackend.domain.Item;
import soma.gstbackend.enums.ItemStatus;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class ItemRequest {
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

    @NotNull(message = "type이 없습니다.")
    public final String type;

    public Item toEntity(Member member, Category category) {
        Item item = Item.createItem(member, category, ItemStatus.ready, s3Key, isPublic, title, descript, type);
        return item;
    }
}
