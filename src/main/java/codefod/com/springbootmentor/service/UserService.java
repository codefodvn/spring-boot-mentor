package codefod.com.springbootmentor.service;

import codefod.com.springbootmentor.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getAllUsers() {
        userRepository.findAll();
    }
}
