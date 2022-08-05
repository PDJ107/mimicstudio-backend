package soma.gstbackend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
public class Member extends CustomEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    private String account;
    private String password;
    private String phoneNumber;
    private String email;
}