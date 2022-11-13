package soma.gstbackend.controller;

import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.domain.*;
import soma.gstbackend.dto.item.*;
import soma.gstbackend.dto.page.PageResponse;
import soma.gstbackend.enums.ItemStatus;
import soma.gstbackend.service.CategoryService;
import soma.gstbackend.service.ItemService;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;
import soma.gstbackend.util.form.ItemMessageForm;
import soma.gstbackend.util.form.MessageForm;
import soma.gstbackend.util.MessageProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/3d-items")
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final MessageProcessor messageProcessor;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity create(HttpServletRequest request, @RequestBody @Valid ItemRequest itemRequest) throws Exception {

        Category category = categoryService.findCategory(itemRequest.categoryId);

        Long member_id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        Member member = memberService.findMember(member_id);

        Item item = itemRequest.toEntity(member, category);

        // 아이템 등록
        itemService.join(item);

        return ResponseEntity.accepted().body(ItemResponse.from(item));
    }

    @GetMapping("/check-item")
    public ResponseEntity checkItem(@RequestParam("member_id") Long member_id, @RequestParam("item_id") Long item_id) throws Exception {
        System.out.println(member_id + " " + item_id);
        itemService.validateItem(member_id, item_id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queue")
    public ResponseEntity enqueueItem(@RequestBody @Valid ItemQueueRequest request) throws Exception {

        // update item info
        itemService.enqueue(request.getItemId(), request.getS3Key());

        // SQS 메시지 전송
        MessageForm messageForm = new ItemMessageForm(request.getItemId(), request.getS3Key());
        messageProcessor.send(messageForm);

        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/status")
    public ResponseEntity updateState(@PathVariable Long id, @RequestBody @Valid ItemStatusRequest request) {
        Item item = itemService.patchItem(id, Item.builder().status(request.getStatus()).build());
        return ResponseEntity.ok().body(ItemResponse.from(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> read(@PathVariable Long id) throws Exception {
        Item item = itemService.findItem(id);
        return ResponseEntity.ok().body(new ItemResponse().from(item));
    }

    @PostMapping("/list")
    public ResponseEntity<PageResponse> getList(
            HttpServletRequest request,
            @RequestBody(required = false) ItemSearch search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
            ) {
        Long member_id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        Page<Item> itemPage = itemService.findItems(member_id, search, PageRequest.of(page, size));
        return ResponseEntity.ok().body(ItemResponse.fromPage(itemPage));
    }

    @PostMapping("/list/public")
    public ResponseEntity<PageResponse> getPublicList(
            @RequestBody(required = false) ItemSearch search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
            ) {
        Page<Item> itemPage = itemService.findPublicItems(search, PageRequest.of(page, size));
        return ResponseEntity.ok().body(ItemResponse.fromPage(itemPage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable Long id) throws Exception {
        itemService.removeItem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody ItemModifyRequest request) {
        Item item = itemService.patchItem(id, request.toEntity());
        return ResponseEntity.ok().body(ItemResponse.from(item));
    }

    @PostMapping("/coin")
    public ResponseEntity apply(@RequestBody @Valid ApplyRequest applyRequest) {
        itemService.applyCoin(applyRequest.toEntity());
        return ResponseEntity.accepted().build();
    }
}
