package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import soma.gstbackend.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ResponseDTO {

    public Long id;
    public Long member_id;
    public ItemStatus status;
    public String categoryName;
    public List<View> views;
    public Boolean isPublic;
    public String title;
    public String descript;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ResponseDTO() {
    }

    public static ResponseDTO from(Item item) {
        return new ResponseDTO(
                item.getId(),
                item.getMember().getId(),
                item.getStatus(),
                item.getCategory().getName(),
                item.getViews(),
                item.getIsPublic(),
                item.getTitle(),
                item.getDescript(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    public Page<ResponseDTO> fromPage(Page<Item> page) {
        List<Item> itemList = page.getContent();
        return new PageImpl<>(
                new ResponseDTO().fromList(itemList),
                page.getPageable(),
                page.getTotalPages()
        );
    }

    public List<ResponseDTO> fromList(List<Item> items) {
        return items.stream().map(item -> from(item)).collect(Collectors.toList());
    }
}