package soma.gstbackend.dto;

import lombok.AllArgsConstructor;
import soma.gstbackend.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ItemResponseDto {

    public Long id;
    public ItemStatus status;
    public Category category;
    public List<View> views;
    public Boolean isPublic;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ItemResponseDto() {
    }

    public ItemResponseDto from(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getStatus(),
                item.getCategory(),
                item.getViews(),
                item.getIsPublic(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    public List<ItemResponseDto> fromList(List<Item> items) {
        return items.stream().map(item -> from(item)).collect(Collectors.toList());
    }
}