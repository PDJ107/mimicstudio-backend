package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.entity.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class MemberRequestDto {
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