package codefod.com.springbootmentor.service;

import codefod.com.springbootmentor.model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    void save(User user);
}
