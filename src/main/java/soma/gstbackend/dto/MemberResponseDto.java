package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import soma.gstbackend.entity.Member;

@AllArgsConstructor
public class MemberResponseDto {

    public String account;
    public String phoneNumber;
    public String email;

    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getAccount(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }
}
