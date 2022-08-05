package soma.gstbackend.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "VIEWTYPE")
@Getter
public class ViewType {

    @Id @GeneratedValue
    @Column(name = "type_id")
    private Long id;

//    @OneToOne(mappedBy = "type", fetch = FetchType.LAZY)
//    private View view;

    private String type;
}
