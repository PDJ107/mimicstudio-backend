package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import soma.gstbackend.domain.ApplyCoin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApplyRequest {

    @NotNull(message = "email이 없습니다.")
    @Size(max = 100, message = "최대 길이는 100 입니다.")
    private String email;

    @NotNull(message = "userType이 없습니다.")
    @Size(max = 200, message = "최대 길이는 200 입니다.")
    private String userType;

    @NotNull(message = "purpose가 없습니다.")
    @Size(max = 200, message = "최대 길이는 200 입니다.")
    private String purpose;

    @NotNull(message = "productDescript가 없습니다.")
    @Size(max = 200, message = "최대 길이는 200 입니다.")
    private String productDescript;

    @NotNull(message = "productUrl이 없습니다.")
    @Size(max = 200, message = "최대 길이는 200 입니다.")
    private String productUrl;

    public ApplyCoin toEntity() {

        return new ApplyCoin(
                email, userType, purpose, productDescript, productUrl
        );
    }
}
