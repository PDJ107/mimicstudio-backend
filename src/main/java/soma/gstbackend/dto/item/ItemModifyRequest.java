package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import soma.gstbackend.domain.Category;
import soma.gstbackend.domain.Item;
import soma.gstbackend.domain.Member;
import soma.gstbackend.enums.ItemStatus;

@AllArgsConstructor
@Getter
public class ItemModifyRequest {
    Boolean isPublic;
    String title;
    String descript;
    String type;
    ItemStatus status;

    public Item toEntity() {
        Item item = Item.createItem(null, null, status, "", isPublic, title, descript, type);
        return item;
    }
}
