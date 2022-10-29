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

    @NotNull(message = "descript이 없습니다.")
    @Size(max = 200, message = "최대 길이는 200 입니다.")
    private String descript;

    public ApplyCoin toEntity(Long memberId) {
        return new ApplyCoin(memberId, descript);
    }
}
