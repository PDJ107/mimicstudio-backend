package soma.gstbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "view_type")
@Getter @Builder
@AllArgsConstructor
public class ViewType {

    @Id @GeneratedValue
    @Column(name = "type_id")
    private Long id;

//    @OneToOne(mappedBy = "type", fetch = FetchType.LAZY)
//    private View view;

    private String type;

    protected ViewType() {
    }
}
