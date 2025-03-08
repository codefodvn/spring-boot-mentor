package codefod.com.springbootmentor.config.security.config;

import codefod.com.springbootmentor.config.security.custom.CustomAuthenticationProvider;
import codefod.com.springbootmentor.repository.PermissionRepository;
import codefod.com.springbootmentor.repository.RolePermissionRepository;
import codefod.com.springbootmentor.repository.RoleRepository;
import codefod.com.springbootmentor.repository.UserRepository;
import codefod.com.springbootmentor.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userRepository, roleRepository, userRoleRepository,
                permissionRepository, rolePermissionRepository, passwordEncoder);
    }

}
