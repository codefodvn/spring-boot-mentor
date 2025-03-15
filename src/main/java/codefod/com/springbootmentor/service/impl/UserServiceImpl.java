package codefod.com.springbootmentor.service.impl;

import codefod.com.springbootmentor.model.User;
import codefod.com.springbootmentor.service.UserService;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserService userService;

    @Override
    public Optional<User> findByEmail(String email) {
        userService.save(new User());
        return Optional.empty();
    }

    @Transactional
    @Override
    public void save(User user) {

    }
}
