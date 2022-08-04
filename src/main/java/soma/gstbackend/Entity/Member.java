package soma.gstbackend.Entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String account;
    private String password;
    private String phoneNumber;
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
}