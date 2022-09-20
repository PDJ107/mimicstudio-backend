package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.domain.ItemSearch;
import soma.gstbackend.dto.item.ItemRequest;
import soma.gstbackend.dto.item.ItemResponse;
import soma.gstbackend.domain.Category;
import soma.gstbackend.domain.Item;
import soma.gstbackend.domain.Member;
import soma.gstbackend.dto.page.PageResponse;
import soma.gstbackend.service.CategoryService;
import soma.gstbackend.service.ItemService;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.form.ItemMessageForm;
import soma.gstbackend.util.form.MessageForm;
import soma.gstbackend.util.MessageProcessor;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final MessageProcessor messageProcessor;

    @PostMapping("/3d-items")
    public ResponseEntity create(@RequestBody @Valid ItemRequest request) throws Exception {

        Category category = categoryService.findCategory(request.categoryId);
        Member member = memberService.findMember(45L); // test member id - 45L

        Item item = request.toEntity(member, category);

        // 아이템 등록
        itemService.join(item);

        // SQS 메시지 전송
        MessageForm messageForm = new ItemMessageForm(item.getId(), item.getS3Key());
        messageProcessor.send(messageForm);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/3d-items/{id}")
    public ResponseEntity<ItemResponse> read(@PathVariable Long id) throws Exception {
        Item item = itemService.findItem(id);
        return ResponseEntity.ok().body(new ItemResponse().from(item));
    }

    @GetMapping("/3d-items")
    public ResponseEntity<PageResponse> search(
            @RequestBody ItemSearch search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
            ) {
        Page<Item> itemPage = itemService.findItems(search, PageRequest.of(page, size));
        return ResponseEntity.ok().body(ItemResponse.fromPage(itemPage));
    }

    @DeleteMapping("/3d-items/{id}")
    public ResponseEntity remove(@PathVariable Long id) throws Exception {
        itemService.removeItem(id);
        return ResponseEntity.noContent().build();
    }
}
