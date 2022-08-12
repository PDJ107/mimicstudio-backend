package soma.gstbackend.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.dto.ItemRequestDto;
import soma.gstbackend.entity.Category;
import soma.gstbackend.entity.Item;
import soma.gstbackend.entity.ItemStatus;
import soma.gstbackend.entity.Member;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Disabled
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired EntityManager em;
    @Autowired CategoryService categoryService;


    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Test
    //@Rollback(false)
    @DisplayName("아이템 & 카테고리 등록")
    public void create() throws Exception {
        // given
        Category category = new Category(0L, "test-category");
        Member member = Member.builder().id(123L).build();

        Item item = Item.builder()
                .id(2L).category(category).member(member).status(ItemStatus.generated).isPublic(false)
                .createdAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2022, 8, 12, 12, 34, 56))
                .build();

        // when
        categoryService.join(category);
        itemService.join(item);

        // then
        assertTrue(itemService.findItem(item.getId()).equals(item));
        assertTrue(categoryService.findCategory(0L).equals(category));
    }

}