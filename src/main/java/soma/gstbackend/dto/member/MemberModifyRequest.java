package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import soma.gstbackend.domain.Member;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
public class MemberModifyRequest {
    public final String account;
    public final String phoneNumber;
    public final String email;

    public Member toEntity() {
        return Member.builder()
                .account(account)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}
