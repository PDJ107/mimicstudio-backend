package soma.gstbackend.Entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToOne
    private Category parent;
}
