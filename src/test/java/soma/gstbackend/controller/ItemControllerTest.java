package soma.gstbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import soma.gstbackend.dto.ItemRequestDto;
import soma.gstbackend.entity.*;
import soma.gstbackend.service.CategoryService;
import soma.gstbackend.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ItemControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean ItemService itemService;
    @MockBean CategoryService categoryService;

    @Test
    @DisplayName("3D 아이템 생성")
    void create() throws Exception {
        // given
        Category category = new Category(0L, "Test-Category");
        given(categoryService.findCategory(0L))
                .willReturn(category);

        // when
        ItemRequestDto request = new ItemRequestDto("/0/20220812123456", false, 0L);
        ResultActions result = this.mockMvc.perform(
                post("/3d-items/")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isAccepted())
                .andDo(document("item-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("s3Key").description("이미지가 저장된 s3 디렉토리 key"),
                                fieldWithPath("isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("categoryId").description("현재 DB 상에 존재하는 Category ID")
                        )
                ));
    }

    @Test
    @DisplayName("3D 아이템 1개 조회")
    void read() throws Exception {
        // given
        Category category = new Category(0L, "test-category");
        Member member = Member.builder().id(123L).build();

        Item item = Item.builder()
                .id(2L).category(category).member(member).status(ItemStatus.enqueue).isPublic(false)
                .createdAt(LocalDateTime.of(2022, 8, 15, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 15, 12, 34, 56))
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
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("3D 아이템 ID"),
                                fieldWithPath("member_id").description("회원 ID"),
                                fieldWithPath("status").description("3D 아이템의 생성 상태"),
                                fieldWithPath("categoryName").description("3D 아이템이 속한 카테고리 이름"),
                                fieldWithPath("views").description("현재 3D 아이템의 뷰"),
                                fieldWithPath("isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }

    @Test
    @DisplayName("3D 아이템 모두 조회")
    void readAll() throws Exception {
        // given
        Category category = new Category(0L, "test-category");
        Member member = Member.builder().id(123L).build();

        Item item1 = Item.builder()
                .id(2L).category(category).member(member).status(ItemStatus.generated).isPublic(false)
                .createdAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .build();

        Item item2 = Item.builder()
                .id(3L).category(category).member(member).status(ItemStatus.generating).isPublic(true)
                .createdAt(LocalDateTime.of(2022, 8, 20, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 20, 12, 34, 56))
                .build();

        List<Item> items = List.of(item1, item2);

        given(itemService.findItems())
                .willReturn(items);

        // when
        ResultActions result = this.mockMvc.perform(
                get("/3d-items/")
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("item-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("3D 아이템의 ID"),
                                fieldWithPath("[].member_id").description("회원 ID"),
                                fieldWithPath("[].status").description("3D 아이템의 생성 상태"),
                                fieldWithPath("[].categoryName").description("3D 아이템이 속한 카테고리 이름"),
                                fieldWithPath("[].views").description("현재 3D 아이템의 뷰"),
                                fieldWithPath("[].isPublic").description("3D 아이템 공개 여부"),
                                fieldWithPath("[].createdAt").description("생성일시"),
                                fieldWithPath("[].updatedAt").description("수정일시")
                        )
                ));
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
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}