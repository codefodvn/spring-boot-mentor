package codefod.com.springbootmentor.repository.custom.impl;

import codefod.com.springbootmentor.entity.User;
import codefod.com.springbootmentor.repository.custom.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JdbcTemplate template;
    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryCustomImpl(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public User getAllUser() {
        String sql = "SELECT * FROM user";
        return template.queryForObject(sql, User.class);
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }
}
