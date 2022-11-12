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
        return Item.builder().status(status).isPublic(isPublic).title(title).descript(descript).type(type).build();
    }
}
