package soma.gstbackend.util.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemMessageForm extends MessageForm {
    private Long itemId;
    private String s3Key;
}
