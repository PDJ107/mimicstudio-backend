package soma.gstbackend.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "view")
@Getter @SuperBuilder
public class View extends CustomEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ViewType type;

    private String endpoint;

    protected View() {
    }

    public void setItem(Item item) {
        this.item = item;
        item.getViews().add(this);
    }

    public void setType(ViewType type) {
        this.type = type;
//        type.setView(this);
    }
}
