package codefod.com.springbootmentor.service;

import codefod.com.springbootmentor.dto.LoginRequest;
import codefod.com.springbootmentor.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
