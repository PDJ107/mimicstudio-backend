package soma.gstbackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.dto.ItemRequestDto;
import soma.gstbackend.entity.Category;
import soma.gstbackend.entity.Item;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired EntityManager em;

    @Autowired CategoryService categoryService;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Test
    //@Rollback(false)
    public void 아이템_등록() throws Exception {
        // given
        Long categoryId = 0L;
        ItemRequestDto itemRequestDto = new ItemRequestDto("/0/1234444", false, categoryId);
        Category category = categoryService.findCategory(itemRequestDto.categoryId);

        //em.merge(category);
        Item item = itemRequestDto.toEntity(category);

        // when
        itemService.join(item);

        // then
        assertTrue(itemRequestDto.categoryId == categoryId);
        assertTrue(item.getCategory().getId() == categoryId);
    }

}