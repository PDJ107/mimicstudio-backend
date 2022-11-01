package soma.gstbackend.dto.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import soma.gstbackend.domain.*;
import soma.gstbackend.dto.page.PageResponse;
import soma.gstbackend.enums.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemResponse {

    public Long id;
    public Long member_id;
    public ItemStatus status;
    public String categoryName;
    public List<View> views;
    public Boolean isPublic;
    public String title;
    public String descript;
    public String type;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ItemResponse() {
    }

    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getMember().getId(),
                item.getStatus(),
                item.getCategory().getName(),
                item.getViews(),
                item.getIsPublic(),
                item.getTitle(),
                item.getDescript(),
                item.getType(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    public static PageResponse fromPage(Page<Item> page) {
        List<ItemResponse> contents = fromList(page.getContent());
        return new PageResponse<ItemResponse>(
                contents,
                page.getPageable(),
                page.isLast(),
                page.isFirst(),
                page.isEmpty(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements()
        );
    }

    public static List<ItemResponse> fromList(List<Item> items) {
        return items.stream().map(item -> from(item)).collect(Collectors.toList());
    }
}