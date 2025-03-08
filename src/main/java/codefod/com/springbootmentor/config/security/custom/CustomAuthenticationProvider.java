package codefod.com.springbootmentor.config.security.custom;

import codefod.com.springbootmentor.entity.Permission;
import codefod.com.springbootmentor.entity.Role;
import codefod.com.springbootmentor.entity.RolePermission;
import codefod.com.springbootmentor.entity.User;
import codefod.com.springbootmentor.entity.UserRole;
import codefod.com.springbootmentor.model.CredentialPayload;
import codefod.com.springbootmentor.repository.PermissionRepository;
import codefod.com.springbootmentor.repository.RolePermissionRepository;
import codefod.com.springbootmentor.repository.RoleRepository;
import codefod.com.springbootmentor.repository.UserRepository;
import codefod.com.springbootmentor.repository.UserRoleRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        final String email = authentication.getPrincipal()
                .toString();
        final String password = authentication.getCredentials()
                .toString();

        final User user = userRepository.findByEmail(email);
        System.out.println("user.getPassword(): " + passwordEncoder.encode("password123"));

        if (password == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        final CredentialPayload payload = CredentialPayload.builder().
                userId(user.getUserId())
                .email(user.getEmail())
                .build();

        List<SimpleGrantedAuthority> roles = getRoles(user);
        return new UsernamePasswordAuthenticationToken(email, payload,
                roles);//why must be use getRoles function ???
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<SimpleGrantedAuthority> getRoles(User user) {
        Set<UserRole> userRoleEntities = userRoleRepository.findByUser_UserId(user.getUserId());

        Set<Long> ids = userRoleEntities.stream()
                .map(userRoleEntity -> userRoleEntity.getRole().getId())
                .collect(Collectors.toSet());

        Set<Role> roleEntities = roleRepository.findAllByIdIn(ids);
        List<SimpleGrantedAuthority> rolesAuthorities = roleEntities.stream()
                .map(item -> new SimpleGrantedAuthority(item.getName())).toList();

        List<SimpleGrantedAuthority> permissionAuthorities;

        if (rolesAuthorities.stream()
                .anyMatch(item -> item.getAuthority().equals("ROLE_ADMIN"))) {
            permissionAuthorities = permissionRepository.findAll().stream()
                    .map(item -> new SimpleGrantedAuthority(item.getName()))
                    .toList();
        } else {
            Set<Long> roleIds = roleEntities.stream().map(Role::getId)
                    .collect(Collectors.toSet());
            Set<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleIdIn(
                    roleIds);

            Set<Long> permissionIds = rolePermissions.stream()
                    .map(rolePermissionEntity -> rolePermissionEntity.getPermission().getId())
                    .collect(Collectors.toSet());
            Set<Permission> permissionEntities = permissionRepository.findAllByIdIn(permissionIds);

            permissionAuthorities = permissionEntities.stream()
                    .map(item -> new SimpleGrantedAuthority(item.getName()))
                    .toList();
        }
        return Stream.concat(rolesAuthorities.stream(), permissionAuthorities.stream()).toList();
    }
}
