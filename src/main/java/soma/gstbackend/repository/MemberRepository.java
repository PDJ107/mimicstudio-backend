package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.entity.Item;
import soma.gstbackend.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public void remove(Long id) {
        em.find(Member.class, id).setDeleted(true);
    }

    public Boolean isDeleted(Long id) {
        return em.find(Member.class, id).isDeleted() == true;
    }

    public Member findByAccount(String account) {
        return em.createQuery("select m from Member m where m.account = :account", Member.class)
                .setParameter("account", account)
                .getSingleResult();
    }
}
