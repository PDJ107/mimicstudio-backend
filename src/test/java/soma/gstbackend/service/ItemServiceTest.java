package soma.gstbackend.service;

import org.junit.jupiter.api.BeforeAll;
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
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired EntityManager em;
    @Autowired CategoryService categoryService;
    @Autowired MemberService memberService;

    private static Member testMember1, testMember2;
    private static Category testCategory1, testCategory2;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @BeforeAll
    static void setTestVariable() {
        testMember1 = Member.builder()
                .account("abcd").password("12345678").email("test@gmail.com").isDeleted(false).build();
        testMember2 = Member.builder()
                .account("qewr").password("9876543").email("qewr@naver.com").isDeleted(false).build();
        testCategory1 = Category.builder().id(88888L).name("test-category1").build();
        testCategory1 = Category.builder().id(99999L).name("test-category2").build();
    }

    @Test
    @DisplayName("아이템 등록")
    void create() throws Exception {
        // given
        memberService.join(testMember1);
        categoryService.join(testCategory1);

        Item item = Item.builder()
                .status(ItemStatus.generated).isPublic(false)
                .build();
        item.setCategory(testCategory1);
        item.setMember(testMember1);

        // when
        itemService.join(item);

        // then
        assertTrue(itemService.findItem(item.getId()).equals(item));
        assertTrue(categoryService.findCategory(testCategory1.getId()).equals(testCategory1));
    }

    @Test
    @DisplayName("아이템 조회")
    public void findItem() throws Exception {
        // given
        if(testMember1.getId() == null) memberService.join(testMember1);
        if(testCategory1.getId() == null) categoryService.join(testCategory1);

        // when

        // then

    }

    @Test
    @DisplayName("아이템 모두 조회")
    public void findItems() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("아이템 삭제")
    public void removeItem() throws Exception {
        // given

        // when

        // then

    }
}