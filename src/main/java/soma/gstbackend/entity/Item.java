package soma.gstbackend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter @SuperBuilder
public class Item extends CustomEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder.Default
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<View> views = new ArrayList<>();

    private Boolean isPublic;

    private String title;

    private String descript;

    protected Item() {
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

    public static Item createItem(Member member, Category category, ItemStatus status, String s3Key, Boolean isPublic, String title, String descript) {
        Item item = new Item();
        item.setMember(member);
        item.setCategory(category);
        item.status = status;
        item.s3Key = s3Key;
        item.isPublic = isPublic;
        item.title = title;
        item.descript = descript;
        return item;
    }
}