package soma.gstbackend.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.domain.*;
import soma.gstbackend.enums.ItemStatus;
import soma.gstbackend.enums.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired CategoryService categoryService;
    @Autowired MemberService memberService;

//    static {
//        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
//    }

    private Member getTestMember(String account, String password, String email) throws Exception {
        Member testMember = Member.builder()
                .account(account).password(password).email(email).role(Role.GUEST).build();
        memberService.join(testMember);
        return testMember;
    }

    private Category getTestCategory(Long id, String name) throws Exception {
        Category testCategory = Category.builder().id(id).name(name).build();
        categoryService.join(testCategory);
        return testCategory;
    }

    private Item getTestItem(Member member, Category category, ItemStatus status, String s3key, boolean isPublic) throws Exception{
        Item item = Item.createItem(member, category, status, s3key, isPublic, "testTitle", "testDescript", "타입");
        itemService.join(item);
        return item;
    }

    @Test
    @DisplayName("아이템 등록")
    void create() throws Exception {
        // given
        Member testMember = getTestMember("abcd", "12345678", "test@gamil.com");
        Category testCategory = getTestCategory(99999L, "test-category");
        //String s3key = testMember.getId() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Item item = Item.createItem(testMember, testCategory, ItemStatus.complete, "/0/20220801123456", false, "testTitle", "testDescript", "타입");

        // when
        itemService.join(item);

        // then
        assertTrue(itemService.findItem(item.getId()).equals(item));
        assertTrue(categoryService.findCategory(testCategory.getId()).equals(testCategory));
    }

    @Test
    @DisplayName("아이템 조회")
    public void findItem() throws Exception {
        // given
        Member testMember = getTestMember("abcd", "12345678", "test@gamil.com");
        Category testCategory = getTestCategory(99999L, "test-category");
        //String s3key = testMember.getId() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Item testItem = getTestItem(testMember, testCategory, ItemStatus.complete, "/0/20220801123456", false);

        // when
        Item item = itemService.findItem(testItem.getId());

        // then
        assertTrue(item.equals(testItem));
    }

    @Test
    @DisplayName("아이템 모두 조회")
    public void findItems() throws Exception {
        // given
        Member testMember = getTestMember("abcd", "12345678", "test@gamil.com");
        Category testCategory = getTestCategory(99999L, "test-category");

        ItemSearch search = ItemSearch.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Item> currItems = itemService.findPublicItems(search, pageable);
        long currItemNum = currItems.getTotalElements();
        int lastPage = currItems.getTotalPages() - 1;

        //String s3key = testMember.getId() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Item testItem = getTestItem(testMember, testCategory, ItemStatus.complete, "/0/20220801123456", true);
        Item testItem2 = getTestItem(testMember, testCategory, ItemStatus.generating, "/0/20220805123456", true);
        Item testItem3 = getTestItem(testMember, testCategory, ItemStatus.enqueue, "/0/20220805123456", true);

        // when
        pageable = PageRequest.of(0, (int)currItemNum + 5);
        List<Item> items = itemService.findPublicItems(search, pageable).getContent();

        // then
        assertEquals(items.size() - currItemNum, 3);

        // 정렬 추가 후 테스트 수정
        //assertTrue(items.contains(testItem));
        //assertTrue(items.contains(testItem2));
        //assertTrue(items.contains(testItem3));

    }

    @Test
    @DisplayName("아이템 삭제")
    public void removeItem() throws Exception {
        // given
        Member testMember = getTestMember("abcd", "12345678", "test@gamil.com");
        Category testCategory = getTestCategory(99999L, "test-category");
        //String s3key = testMember.getId() + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Item testItem = getTestItem(testMember, testCategory, ItemStatus.complete, "/0/20220801123456", true);
        Item testItem2 = getTestItem(testMember, testCategory, ItemStatus.generating, "/0/20220805123456", true);
        Item testItem3 = getTestItem(testMember, testCategory, ItemStatus.enqueue, "/0/20220805123456", true);

        ItemSearch search = ItemSearch.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        itemService.removeItem(testItem.getId());
        itemService.removeItem(testItem2.getId());
        List<Item> items = itemService.findPublicItems(search, pageable).getContent();

        // then
        assertFalse(items.contains(testItem));
        assertFalse(items.contains(testItem2));
        assertTrue(items.contains(testItem3));
    }
}