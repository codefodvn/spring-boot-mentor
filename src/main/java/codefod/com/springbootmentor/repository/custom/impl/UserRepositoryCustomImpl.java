package codefod.com.springbootmentor.repository.custom.impl;

import codefod.com.springbootmentor.entity.User;
import codefod.com.springbootmentor.repository.custom.UserRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JdbcTemplate template;

    public UserRepositoryCustomImpl(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public User getAllUser() {
        String sql = "SELECT * FROM user";
        return template.queryForObject(sql, User.class);
    }
}
