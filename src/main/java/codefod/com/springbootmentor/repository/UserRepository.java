package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  {
    User findByUsername(String username);
    User findByEmail(String email);
}
