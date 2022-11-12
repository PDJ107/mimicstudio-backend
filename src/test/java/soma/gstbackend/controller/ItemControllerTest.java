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
import soma.gstbackend.dto.item.ItemRequest;
import soma.gstbackend.domain.*;
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
    @DisplayName("3D 아이템 생성")
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
        ItemRequest request = new ItemRequest(false, 0L, "testTitle", "testDescript", "타입");
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
                                fieldWithPath("isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("categoryId").description("현재 DB 상에 존재하는 Category ID"),
                                fieldWithPath("title").description("3D 아이템 제목"),
                                fieldWithPath("descript").description("3D 아이템 설명"),
                                fieldWithPath("type").description("3D 아이템 타입")
                        ),
                        responseFields(
                                fieldWithPath("id").description("3D 아이템 ID"),
                                fieldWithPath("member_id").description("회원 ID"),
                                fieldWithPath("status").description("3D 아이템의 생성 상태"),
                                fieldWithPath("categoryName").description("3D 아이템이 속한 카테고리 이름"),
                                fieldWithPath("views").description("현재 3D 아이템의 뷰"),
                                fieldWithPath("isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("title").description("3D 아이템 제목"),
                                fieldWithPath("descript").description("3D 아이템 설명"),
                                fieldWithPath("type").description("3D 아이템 타입"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    @DisplayName("3D 아이템 1개 조회")
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
                                fieldWithPath("id").description("3D 아이템 ID"),
                                fieldWithPath("member_id").description("회원 ID"),
                                fieldWithPath("status").description("3D 아이템의 생성 상태"),
                                fieldWithPath("categoryName").description("3D 아이템이 속한 카테고리 이름"),
                                fieldWithPath("views").description("현재 3D 아이템의 뷰"),
                                fieldWithPath("isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("title").description("3D 아이템 제목"),
                                fieldWithPath("descript").description("3D 아이템 설명"),
                                fieldWithPath("type").description("3D 아이템 타입"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    private void ItemPageDocument(ResultActions result, String name) throws Exception {
        result.andDo(document(name,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("itemTitle").description("3D 아이템 제목으로 검색"),
                        fieldWithPath("categoryName").description("카테고리 이름으로 검색")
                ),
                responseFields(
                        fieldWithPath("contents[].id").description("3D 아이템의 ID"),
                        fieldWithPath("contents[].member_id").description("회원 ID"),
                        fieldWithPath("contents[].status").description("3D 아이템의 생성 상태"),
                        fieldWithPath("contents[].categoryName").description("3D 아이템이 속한 카테고리 이름"),
                        fieldWithPath("contents[].views").description("현재 3D 아이템의 뷰"),
                        fieldWithPath("contents[].isPublic").description("3D 아이템 공개 여부"),
                        fieldWithPath("contents[].title").description("3D 아이템 제목"),
                        fieldWithPath("contents[].descript").description("3D 아이템 설명"),
                        fieldWithPath("contents[].type").description("3D 아이템 타입"),
                        fieldWithPath("contents[].createdAt").description("생성일시"),
                        fieldWithPath("contents[].updatedAt").description("수정일시"),
                        fieldWithPath("pageable.sort.empty").description("정렬 설정 여부"),
                        fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                        fieldWithPath("pageable.sort.unsorted").description("정렬 여부"),
                        fieldWithPath("pageable.offset").description("현재 페이지 첫번째 아이템 번호"),
                        fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
                        fieldWithPath("pageable.pageSize").description("한 페이지에 표시되는 최대 아이템 수"),
                        fieldWithPath("pageable.paged").description(""),
                        fieldWithPath("pageable.unpaged").description(""),
                        fieldWithPath("last").description("마지막 페이지 여부"),
                        fieldWithPath("first").description("첫번째 페이지 여부"),
                        fieldWithPath("empty").description("빈 페이지 여부"),
                        fieldWithPath("totalPages").description("전체 페이지 수"),
                        fieldWithPath("totalElements").description("전체 아이템 수"),
                        fieldWithPath("numberOfElements").description("현재 페이지 아이템 수")
                )
        ));
    }

    private List<Item> generateItemList(long count, Category category, Member member) {
        List<Item> items = new ArrayList<>();
        for(long i = 0; i < count; ++i) {
            items.add(
                Item.builder()
                        .id(i).category(category).member(member).status(ItemStatus.complete).isPublic(false)
                        .createdAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                        .updatedAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                        .title("testTitle" + i)
                        .descript("testDescript" + i)
                        .isPublic(true)
                        .build()
            );
        }
        return items;
    }

    @Test
    @DisplayName("3D 아이템 모두 조회")
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
    @DisplayName("3D 아이템 페이지네이션")
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
    @DisplayName("3D 아이템 삭제")
    void remove() throws Exception {
        // given

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
    @DisplayName("코인 신청")
    public void applyCoin() throws Exception {
        // given
        ApplyRequest request = new ApplyRequest("test@test.com", "일반 사용자", "상품 소개 용도", "피규어", "");
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
                                fieldWithPath("email").description("신청 이메일"),
                                fieldWithPath("userType").description("사용자 유형"),
                                fieldWithPath("purpose").description("사용 목적"),
                                fieldWithPath("productDescript").description("사용할 물품 설명"),
                                fieldWithPath("productUrl").description("상품 판매 페이지 url")
                        )
                ));

    }
}