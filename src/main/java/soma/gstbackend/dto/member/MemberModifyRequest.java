package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class MemberModifyRequest {
    @NotNull(message = "account 값이 Null 입니다.")
    public final String account;

    @NotNull(message = "phoneNumber 값이 Null 입니다.")
    public final String phoneNumber;

    @NotNull(message = "email 값이 Null 입니다.")
    public final String email;

    public Member toEntity() {
        return Member.builder()
                .account(account)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}
