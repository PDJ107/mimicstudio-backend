package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class RequestDTO {
    @NotNull(message = "account 값이 Null 입니다.")
    public final String account;

    @NotNull(message = "password 값이 Null 입니다.")
    public final String password;

    public final String phoneNumber;
    public final String email;

    public Member toEntity() {
        return Member.builder()
                .account(account)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}