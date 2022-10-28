package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.domain.AuthToken;
import soma.gstbackend.domain.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRepository {
    private final EntityManager em;

    public AuthToken findToken(Long id) { return em.find(AuthToken.class, id); }

    public void save(AuthToken authToken) { em.persist(authToken); }
}
