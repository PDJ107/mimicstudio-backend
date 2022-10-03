package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.domain.Member;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        try {
            return em.createQuery("select m from Member m where m.id = :id and m.isDeleted = false", Member.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    public void remove(Long id) {
        em.find(Member.class, id).setDeleted(true);
    }

    public Boolean isDeleted(Long id) {
        return em.find(Member.class, id).isDeleted() == true;
    }

    public Member findByAccount(String account) {
        try {
            return em.createQuery("select m from Member m where m.account = :account and m.isDeleted = false", Member.class)
                    .setParameter("account", account)
                    .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery("select m from Member m where m.email = :email and m.isDeleted = false", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }
}
