package soma.gstbackend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter @SuperBuilder
public class Category {
    @Id
    @Column(name = "category_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    protected Category() {
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChild(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
