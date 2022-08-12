package soma.gstbackend.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter @SuperBuilder
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

    protected Member() {
    }
}