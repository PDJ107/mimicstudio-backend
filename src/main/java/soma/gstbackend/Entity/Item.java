package soma.gstbackend.Entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private ItemStatus status;

    private String s3Key;

    private Category category;

    private Boolean isPublic;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

}