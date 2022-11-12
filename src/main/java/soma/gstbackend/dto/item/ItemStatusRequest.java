package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soma.gstbackend.enums.ItemStatus;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemStatusRequest {
    @NotNull(message = "status가 없습니다.")
    ItemStatus status;
}
