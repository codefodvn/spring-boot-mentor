package codefod.com.springbootmentor.service.impl;

import codefod.com.springbootmentor.model.User;
import codefod.com.springbootmentor.service.UserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }
}
