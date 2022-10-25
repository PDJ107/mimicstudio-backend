package soma.gstbackend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "일반 회원"),
    USER("ROLE_USER", "3D 제작 가능 회원"),
    SUPER_USER("ROLE_SUPER_USER", "모든 3D 제작 가능 회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
