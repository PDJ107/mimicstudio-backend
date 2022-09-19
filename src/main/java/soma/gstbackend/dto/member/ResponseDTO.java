package soma.gstbackend.dto.member;

import lombok.AllArgsConstructor;
import soma.gstbackend.domain.Member;

@AllArgsConstructor
public class ResponseDTO {

    public String account;
    public String phoneNumber;
    public String email;

    public static ResponseDTO from(Member member) {
        return new ResponseDTO(
                member.getAccount(),
                member.getPhoneNumber(),
                member.getEmail()
        );
    }
}
