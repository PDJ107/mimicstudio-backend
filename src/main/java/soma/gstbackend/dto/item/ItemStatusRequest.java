package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import soma.gstbackend.enums.ItemStatus;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class ItemStatusRequest {
    @NotNull(message = "itemId가 없습니다.")
    Long itemId;

    @NotNull(message = "status가 없습니다.")
    ItemStatus status;
}
