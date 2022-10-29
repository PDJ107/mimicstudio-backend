package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.domain.*;
import soma.gstbackend.dto.item.ApplyRequest;
import soma.gstbackend.dto.item.ItemRequest;
import soma.gstbackend.dto.item.ItemResponse;
import soma.gstbackend.dto.page.PageResponse;
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

        // SQS 메시지 전송
        //MessageForm messageForm = new ItemMessageForm(item.getId(), item.getS3Key());
        //messageProcessor.send(messageForm);

        return ResponseEntity.accepted().build();
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

    @PostMapping("/coin")

    public ResponseEntity apply(HttpServletRequest request, @RequestBody @Valid ApplyRequest applyRequest) {

        Long memberId = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        itemService.applyCoin(applyRequest.toEntity(memberId));
        return ResponseEntity.accepted().build();
    }
}
