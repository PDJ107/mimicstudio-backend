package soma.gstbackend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ITEM")
@Getter
public class Item extends CustomEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String s3Key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<View> views = new ArrayList<>();

    private Boolean isPublic;

    protected Item() {
    }

    public Item(ItemStatus status, String s3Key, Boolean isPublic) {
        this.status = status;
        this.s3Key = s3Key;
        this.isPublic = isPublic;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getItems().add(this);
    }

    public void setCategory(Category category) {
        if(this.category != null) {
            this.category.getItems().remove(this);
        }
        this.category = category;
        category.getItems().add(this);
    }
}