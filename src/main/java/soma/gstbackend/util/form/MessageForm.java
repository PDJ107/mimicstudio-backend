package soma.gstbackend.util.form;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageForm {
    private LocalDateTime requestTime = LocalDateTime.now();
}
