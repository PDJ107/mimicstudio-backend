package soma.gstbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "apply_coin")
@Getter
public class ApplyCoin extends CustomEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    private String email;
    private String userType;
    private String purpose;
    private String productDescript;
    private String productUrl;

    protected ApplyCoin() {}

    public ApplyCoin(String email, String userType, String purpose, String productDescript, String productUrl) {
        this.email = email;
        this.userType = userType;
        this.purpose = purpose;
        this.productDescript = productDescript;
        this.productUrl = productUrl;
    }

    public void update(String userType, String purpose, String productDescript, String productUrl) {
        this.userType = userType;
        this.purpose = purpose;
        this.productDescript = productDescript;
        this.productUrl = productUrl;
    }
}
