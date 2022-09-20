package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import soma.gstbackend.domain.Member;

@AllArgsConstructor
public class MemberResponse {

    public String account;
    public String phoneNumber;
    public String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getAccount(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }
}
