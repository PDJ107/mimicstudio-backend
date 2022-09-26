package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class LoginDTO {
    @NotNull(message = "email 값이 Null 입니다.")
    public final String email;

    @NotNull(message = "password 값이 Null 입니다.")
    public final String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}