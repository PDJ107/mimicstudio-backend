package soma.gstbackend.util;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageForm {
    private LocalDateTime requestTime = LocalDateTime.now();
}
