package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.entity.Category;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Category findOne(Long id) {
        return em.find(Category.class, id);
    }
}
