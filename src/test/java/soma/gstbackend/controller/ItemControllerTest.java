package soma.gstbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import soma.gstbackend.dto.item.ApplyRequest;
import soma.gstbackend.dto.item.ItemModifyRequest;
import soma.gstbackend.dto.item.ItemRequest;
import soma.gstbackend.domain.*;
import soma.gstbackend.dto.item.ItemStatusRequest;
import soma.gstbackend.enums.ItemStatus;
import soma.gstbackend.service.CategoryService;
import soma.gstbackend.service.ItemService;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;
import soma.gstbackend.util.MessageProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static soma.gstbackend.ApiDocumentUtils.getDocumentRequest;
import static soma.gstbackend.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(ItemController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean ItemService itemService;
    @MockBean MemberService memberService;
    @MockBean CategoryService categoryService;
    @MockBean MessageProcessor messageProcessor;
    @MockBean JwtUtil jwtUtil;

    @Test
    @DisplayName("3D ????????? ??????")
    void create() throws Exception {
        // given
        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member testMember = Member.builder()
                .account("test-member").password("test").email("test@test.com").build();

        given(categoryService.findCategory(0L))
                .willReturn(category);
        given(memberService.findMember(45L))
                .willReturn(testMember);
        given(jwtUtil.getIdFromToken(any()))
                .willReturn(45L);

        // when
        ItemRequest request = new ItemRequest(false, 0L, "testTitle", "testDescript", "??????");
        ResultActions result = this.mockMvc.perform(
                post("/3d-items/")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isAccepted())
                .andDo(document("item-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("categoryId").description("?????? DB ?????? ???????????? Category ID"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("3D ????????? ID"),
                                fieldWithPath("member_id").description("?????? ID"),
                                fieldWithPath("account").description("?????? ??????"),
                                fieldWithPath("status").description("3D ???????????? ?????? ??????"),
                                fieldWithPath("categoryName").description("3D ???????????? ?????? ???????????? ??????"),
                                fieldWithPath("views").description("?????? 3D ???????????? ???"),
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????"),
                                fieldWithPath("createdAt").description("????????????"),
                                fieldWithPath("updatedAt").description("????????????")
                        )
                ));
    }

    @Test
    @DisplayName("3D ????????? 1??? ??????")
    void read() throws Exception {
        // given
        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member member = Member.builder().id(123L).build();

        Item item = Item.builder()
                .id(2L).category(category).member(member).status(ItemStatus.enqueue).isPublic(false)
                .createdAt(LocalDateTime.of(2022, 8, 15, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 15, 12, 34, 56))
                .title("testTitle")
                .descript("testDescript")
                .build();

        given(itemService.findItem(2L))
                .willReturn(item);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/3d-items/{id}", 2L)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("item-findOne",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").description("3D ????????? ID"),
                                fieldWithPath("member_id").description("?????? ID"),
                                fieldWithPath("account").description("?????? ??????"),
                                fieldWithPath("status").description("3D ???????????? ?????? ??????"),
                                fieldWithPath("categoryName").description("3D ???????????? ?????? ???????????? ??????"),
                                fieldWithPath("views").description("?????? 3D ???????????? ???"),
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????"),
                                fieldWithPath("createdAt").description("????????????"),
                                fieldWithPath("updatedAt").description("????????????")
                        )
                ));
    }

    private void ItemPageDocument(ResultActions result, String name) throws Exception {
        result.andDo(document(name,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("itemTitle").description("3D ????????? ???????????? ??????"),
                        fieldWithPath("categoryName").description("???????????? ???????????? ??????")
                ),
                responseFields(
                        fieldWithPath("contents[].id").description("3D ???????????? ID"),
                        fieldWithPath("contents[].member_id").description("?????? ID"),
                        fieldWithPath("contents[].account").description("?????? ??????"),
                        fieldWithPath("contents[].status").description("3D ???????????? ?????? ??????"),
                        fieldWithPath("contents[].categoryName").description("3D ???????????? ?????? ???????????? ??????"),
                        fieldWithPath("contents[].views").description("?????? 3D ???????????? ???"),
                        fieldWithPath("contents[].isPublic").description("3D ????????? ?????? ??????"),
                        fieldWithPath("contents[].title").description("3D ????????? ??????"),
                        fieldWithPath("contents[].descript").description("3D ????????? ??????"),
                        fieldWithPath("contents[].type").description("3D ????????? ??????"),
                        fieldWithPath("contents[].createdAt").description("????????????"),
                        fieldWithPath("contents[].updatedAt").description("????????????"),
                        fieldWithPath("pageable.sort.empty").description("?????? ?????? ??????"),
                        fieldWithPath("pageable.sort.sorted").description("?????? ??????"),
                        fieldWithPath("pageable.sort.unsorted").description("?????? ??????"),
                        fieldWithPath("pageable.offset").description("?????? ????????? ????????? ????????? ??????"),
                        fieldWithPath("pageable.pageNumber").description("?????? ????????? ??????"),
                        fieldWithPath("pageable.pageSize").description("??? ???????????? ???????????? ?????? ????????? ???"),
                        fieldWithPath("pageable.paged").description(""),
                        fieldWithPath("pageable.unpaged").description(""),
                        fieldWithPath("last").description("????????? ????????? ??????"),
                        fieldWithPath("first").description("????????? ????????? ??????"),
                        fieldWithPath("empty").description("??? ????????? ??????"),
                        fieldWithPath("totalPages").description("?????? ????????? ???"),
                        fieldWithPath("totalElements").description("?????? ????????? ???"),
                        fieldWithPath("numberOfElements").description("?????? ????????? ????????? ???")
                )
        ));
    }

    private Item generateItem(long i, Category category, Member member) {
        return Item.builder()
                .id(i).category(category).member(member).status(ItemStatus.complete).isPublic(false)
                .createdAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .title("testTitle" + i)
                .descript("testDescript" + i)
                .isPublic(true)
                .build();
    }

    private List<Item> generateItemList(long count, Category category, Member member) {
        List<Item> items = new ArrayList<>();
        for(long i = 0; i < count; ++i) {
            items.add(
                generateItem(i, category, member)
            );
        }
        return items;
    }

    @Test
    @DisplayName("3D ????????? ?????? ??????")
    void readAll() throws Exception {
        // given
        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member member = Member.builder().id(123L).build();

        List<Item> items = generateItemList(2, category, member);

        ItemSearch search = new ItemSearch(null, null);
        Pageable pageable = PageRequest.of(0, 10);

        given(itemService.findPublicItems(any(), any()))
                .willReturn(new PageImpl(items, pageable, items.size()));

        // when
        ResultActions result = this.mockMvc.perform(
                post("/3d-items/list/public")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        ItemPageDocument(result.andExpect(status().isOk()), "item-findAll");
    }

    @Test
    @DisplayName("3D ????????? ??????????????????")
    public void readSearch() throws Exception {
        // given
        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member member = Member.builder().id(123L).build();

        List<Item> items = generateItemList(5, category, member);

        ItemSearch search = new ItemSearch(null, null);
        Pageable pageable1 = PageRequest.of(0, 3);
        Pageable pageable2 = PageRequest.of(1, 3);

        // when

        given(itemService.findPublicItems(any(), any()))
                .willReturn(new PageImpl(items.subList(0, 3), pageable1, items.size()));
        ResultActions result1 = this.mockMvc.perform(
                post("/3d-items/list/public?page=0&size=3")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        given(itemService.findPublicItems(any(), any()))
                .willReturn(new PageImpl(items.subList(3, 5), pageable2, items.size()));
        ResultActions result2 = this.mockMvc.perform(
                post("/3d-items/list/public?page=1&size=3")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        ItemPageDocument(result1.andExpect(status().isOk()), "item-pagination1");
        ItemPageDocument(result2.andExpect(status().isOk()), "item-pagination2");
    }

    @Test
    @DisplayName("3D ????????? ??????")
    void remove() throws Exception {
        // given
        doNothing().when(itemService).removeItem(any());

        // when
        ResultActions result = this.mockMvc.perform(
                delete("/3d-items/{id}", 0L)
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("item-remove",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

    @Test
    @DisplayName("3D ????????? ??????")
    public void update() throws Exception {
        // given
        ItemModifyRequest request = new ItemModifyRequest(false, null, null, "modifyType", ItemStatus.complete);

        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member member = Member.builder().id(123L).build();

        Item item = generateItem(0, category, member);

        item.setPublic(request.getIsPublic());
        item.setType(request.getType());
        item.setStatus(request.getStatus());

        given(itemService.patchItem(any(), any()))
                .willReturn(item);

        // when
        ResultActions result = this.mockMvc.perform(
                patch("/3d-items/{id}", 0L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("item-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????"),
                                fieldWithPath("status").description("3D ???????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("3D ????????? ID"),
                                fieldWithPath("member_id").description("?????? ID"),
                                fieldWithPath("account").description("?????? ??????"),
                                fieldWithPath("status").description("3D ???????????? ?????? ??????"),
                                fieldWithPath("categoryName").description("3D ???????????? ?????? ???????????? ??????"),
                                fieldWithPath("views").description("?????? 3D ???????????? ???"),
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????"),
                                fieldWithPath("createdAt").description("????????????"),
                                fieldWithPath("updatedAt").description("????????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ????????????")
    public void updateStatus() throws Exception {
        // given
        ItemStatusRequest request = new ItemStatusRequest(ItemStatus.complete);

        Category category = Category.builder().id(0L).name("Test-Category").build();
        Member member = Member.builder().id(123L).build();

        Item item = generateItem(0, category, member);

        item.setStatus(request.getStatus());

        given(itemService.patchItem(any(), any()))
                .willReturn(item);

        // when
        ResultActions result = this.mockMvc.perform(
                put("/3d-items/{id}/status", 0L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("item-status-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("status").description("3D ???????????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("3D ????????? ID"),
                                fieldWithPath("member_id").description("?????? ID"),
                                fieldWithPath("account").description("?????? ??????"),
                                fieldWithPath("status").description("3D ???????????? ?????? ??????"),
                                fieldWithPath("categoryName").description("3D ???????????? ?????? ???????????? ??????"),
                                fieldWithPath("views").description("?????? 3D ???????????? ???"),
                                fieldWithPath("isPublic").description("3D ????????? ?????? ??????"),
                                fieldWithPath("title").description("3D ????????? ??????"),
                                fieldWithPath("descript").description("3D ????????? ??????"),
                                fieldWithPath("type").description("3D ????????? ??????"),
                                fieldWithPath("createdAt").description("????????????"),
                                fieldWithPath("updatedAt").description("????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    public void applyCoin() throws Exception {
        // given
        ApplyRequest request = new ApplyRequest("test@test.com", "?????? ?????????", "?????? ?????? ??????", "?????????", "");
        doNothing()
                .when(itemService).applyCoin(any());

        // when
        ResultActions result = this.mockMvc.perform(
                post("/3d-items/coin", 0L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isAccepted())
                .andDo(document("item-coin-apply",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("?????? ?????????"),
                                fieldWithPath("userType").description("????????? ??????"),
                                fieldWithPath("purpose").description("?????? ??????"),
                                fieldWithPath("productDescript").description("????????? ?????? ??????"),
                                fieldWithPath("productUrl").description("?????? ?????? ????????? url")
                        )
                ));

    }
}