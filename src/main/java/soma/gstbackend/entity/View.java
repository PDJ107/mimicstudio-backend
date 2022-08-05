package soma.gstbackend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VIEW")
@Getter
public class View extends CustomEntity {

    @Id @GeneratedValue
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ViewType type;

    private String endpoint;

    public void setItem(Item item) {
        this.item = item;
        item.getViews().add(this);
    }

    public void setType(ViewType type) {
        this.type = type;
//        type.setView(this);
    }
}
