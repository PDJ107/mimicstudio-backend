package soma.gstbackend.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenInfoDTO {
    private Long id;
    private String role;
}
