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

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
//@Disabled
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
        Long categoryId = 0L;
        Category category = new Category(categoryId, "test-category");
        ItemRequestDto itemRequestDto = new ItemRequestDto("/0/1234444", false, categoryId);
        Item item = itemRequestDto.toEntity(category);

        // when
        categoryService.join(category);
        itemService.join(item);

        // then
        assertTrue(itemService.findItem(item.getId()).equals(item));
        assertTrue(categoryService.findCategory(categoryId).equals(category));
    }

}