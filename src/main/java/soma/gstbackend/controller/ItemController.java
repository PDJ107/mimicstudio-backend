package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.dto.ItemRequestDto;
import soma.gstbackend.dto.ItemResponseDto;
import soma.gstbackend.entity.Category;
import soma.gstbackend.entity.Item;
import soma.gstbackend.service.CategoryService;
import soma.gstbackend.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @PostMapping("/3d-items")
    public ResponseEntity create(@RequestBody @Valid ItemRequestDto itemRequestDto) throws Exception {
        Category category = categoryService.findCategory(itemRequestDto.categoryId);
        itemService.join(itemRequestDto.toEntity(category));
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/3d-items/{id}")
    public ResponseEntity<ItemResponseDto> read(@PathVariable Long id) throws Exception {
        Item item = itemService.findItem(id);
        return ResponseEntity.ok().body(new ItemResponseDto().from(item));
    }

    @GetMapping("/3d-items")
    public ResponseEntity<List<ItemResponseDto>> readAll() {
        List<Item> items = itemService.findItems();
        return ResponseEntity.ok().body(new ItemResponseDto().fromList(items));
    }

    @DeleteMapping("/3d-items/{id}")
    public ResponseEntity remove(@PathVariable Long id) throws Exception {
        itemService.removeItem(id);
        return ResponseEntity.noContent().build();
    }
}
