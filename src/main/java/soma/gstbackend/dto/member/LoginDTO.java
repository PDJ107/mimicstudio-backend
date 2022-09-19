package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class LoginDTO {
    @NotNull(message = "account 값이 Null 입니다.")
    public final String account;

    @NotNull(message = "password 값이 Null 입니다.")
    public final String password;

    public Member toEntity() {
        return Member.builder()
                .account(account)
                .password(password)
                .build();
    }
}