package soma.gstbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemSearch {
    //private String memberName;
    private String itemTitle;
    private String categoryName;
}
