package soma.gstbackend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
public class Category {
    @Id
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    protected Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChild(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
