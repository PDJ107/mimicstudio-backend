package soma.gstbackend.Entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class View {

    @Id @GeneratedValue
    @Column(name = "view_id")
    private Long id;

    private Item item;

    private ViewType type;

    private String endpoint;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
}
