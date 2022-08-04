package soma.gstbackend.Entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ViewType {

    @Id @GeneratedValue
    @Column(name = "type_id")
    private Long id;

    private String type;
}
