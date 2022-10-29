package soma.gstbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apply_coin")
@Getter
@AllArgsConstructor
public class ApplyCoin extends CustomEntity {
    @Id @Column(name = "member_id")
    private Long memberId;

    private String descript;

    protected ApplyCoin() {}

    public void update(String descript) {
        this.descript = descript;
    }
}
