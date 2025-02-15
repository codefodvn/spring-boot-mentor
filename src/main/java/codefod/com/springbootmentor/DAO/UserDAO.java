package codefod.com.springbootmentor.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public void getAllUser() {
        entityManager.createQuery("SELECT u FROM User u").getResultList();
    }
}
