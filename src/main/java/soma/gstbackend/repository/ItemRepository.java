package soma.gstbackend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import soma.gstbackend.domain.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        //em.merge(item.getCategory());
        em.persist(item);
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i where i.isDeleted = false", Item.class)
                .getResultList();
    }

    public void remove(Long id) {
        em.find(Item.class, id).setDeleted(true);
    }

    public Boolean isDeleted(Long id) {
        return em.find(Item.class, id).isDeleted() == true;
    }
}
