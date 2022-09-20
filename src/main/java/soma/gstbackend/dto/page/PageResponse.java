package soma.gstbackend.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import soma.gstbackend.domain.Item;
import soma.gstbackend.dto.item.ItemResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PageResponse <T> {
    List<T> contents;
    Pageable pageable;
    boolean last;
    boolean first;
    boolean empty;
    long totalPages;
    long totalElements;
    long numberOfElements;
}
