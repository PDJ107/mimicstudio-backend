package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class ItemQueueRequest {
    @NotNull(message = "s3Key가 Null입니다.")
    String s3Key;

    @NotNull(message = "item_id가 Null입니다.")
    Long itemId;
}
