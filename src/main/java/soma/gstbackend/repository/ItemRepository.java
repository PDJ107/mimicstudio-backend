package soma.gstbackend.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import soma.gstbackend.domain.*;

import javax.persistence.EntityManager;
import java.sql.Driver;
import java.util.List;
import java.util.Optional;

import static soma.gstbackend.domain.QItem.item;
import static soma.gstbackend.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    private BooleanExpression memberNameEq(String memberName) {
        if(memberName == null) {
            return null;
        }
        return member.account.eq(memberName);
    }

    private BooleanExpression titleLike(String itemTitle) {
        if(!StringUtils.hasText(itemTitle)) {
            return null;
        }
        return item.title.contains(itemTitle);
    }

    private BooleanExpression categoryEq(String categoryName) {
        if(categoryName == null) {
            return null;
        }
        return item.category.name.eq(categoryName);
    }

    public void save(Item item) {
        //em.merge(item.getCategory());
        em.persist(item);
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public Page<Item> findAll(Long member_id, ItemSearch search, Pageable pageable) {
        QItem item = QItem.item;
        //QMember member = QMember.member;

        List<Item> result =  query.select(item)
                //.leftJoin(item.member, member)
                .from(item)
                .where(
                        item.member.id.eq(member_id),
                        titleLike(search.getItemTitle()),
                        categoryEq(search.getCategoryName()),
                        item.isDeleted.eq(false)
                )
                .orderBy(item.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Item> countQuery = query.select(item)
                .from(item)
                .where(
                        item.member.id.eq(member_id),
                        titleLike(search.getItemTitle()),
                        categoryEq(search.getCategoryName()),
                        item.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetch().size());
    }

    public Page<Item> findPublicAll(ItemSearch search, Pageable pageable) {

        QItem item = QItem.item;
        //QMember member = QMember.member;

        List<Item> result =  query.select(item)
                //.leftJoin(item.member, member)
                .from(item)
                .where(
                        titleLike(search.getItemTitle()),
                        categoryEq(search.getCategoryName()),
                        item.isPublic.eq(true),
                        item.isDeleted.eq(false)
                )
                .orderBy(item.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Item> countQuery = query.select(item)
                //.leftJoin(item.member, member)
                .from(item)
                .where(
                        titleLike(search.getItemTitle()),
                        categoryEq(search.getCategoryName()),
                        item.isPublic.eq(true),
                        item.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetch().size());
    }

    public void remove(Long id) {
        em.find(Item.class, id).setDeleted(true);
    }

    public Boolean isDeleted(Long id) {
        return em.find(Item.class, id).isDeleted() == true;
    }

    public Optional<ApplyCoin> findApplyByEmail(String email) {
        return em.createQuery("select a from ApplyCoin a where a.email = :email", ApplyCoin.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findAny();
    }

    public void saveApply(ApplyCoin apply) {
        em.persist(apply);
    }
}
