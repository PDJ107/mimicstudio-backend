package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import soma.gstbackend.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemResponseDto {

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

    public ItemResponseDto() {
    }

    public ItemResponseDto from(Item item) {
        return new ItemResponseDto(
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

    public List<ItemResponseDto> fromList(List<Item> items) {
        return items.stream().map(item -> from(item)).collect(Collectors.toList());
    }
}