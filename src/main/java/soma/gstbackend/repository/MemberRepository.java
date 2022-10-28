package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    public Optional<Member> findByAccount(String account) {
        List<Member> members = em.createQuery("select m from Member m where m.account = :account and m.isDeleted = false", Member.class)
                .setParameter("account", account)
                .getResultList();
        return members.stream().findAny();
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> members =  em.createQuery("select m from Member m where m.email = :email and m.isDeleted = false", Member.class)
                .setParameter("email", email)
                .getResultList();
        return members.stream().findAny();
    }
}
