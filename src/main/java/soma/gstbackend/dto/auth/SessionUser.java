package soma.gstbackend.dto.auth;

import soma.gstbackend.domain.Member;

import java.io.Serializable;

public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(Member member) {
        name = member.getAccount();
        email = member.getEmail();
    }
}
