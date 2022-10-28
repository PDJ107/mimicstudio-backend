package soma.gstbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "auth_token")
@Getter @Builder
public class AuthToken {
    @Id
    @Column(name = "member_id")
    Long id;

    String refreshToken;

    protected AuthToken() {}

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
