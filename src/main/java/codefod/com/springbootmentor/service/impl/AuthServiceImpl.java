package codefod.com.springbootmentor.service.impl;

import codefod.com.springbootmentor.dto.LoginRequest;
import codefod.com.springbootmentor.dto.LoginResponse;
import codefod.com.springbootmentor.model.CredentialPayload;
import codefod.com.springbootmentor.service.AuthService;
import codefod.com.springbootmentor.service.JWTService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationProvider authenticationProvider;
    private final JWTService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        final Authentication authentication = this.authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(),
                        loginRequest.password()));

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        //Tạo Claims cho JWT
        Map<String, Object> claims = new LinkedHashMap<>();
        if (authentication.getCredentials() instanceof CredentialPayload credentialPayload) {
            claims = buildClaimsFromAuthentication(credentialPayload.getEmail(),
                    credentialPayload.getUserId(), roles);
        }
        claims.put("token_type", "access_token");

        String token = jwtService.generateToken(claims, authentication);
        Long expiresIn = System.currentTimeMillis() + 30;

        return new LoginResponse(token, "", expiresIn, roles);
    }

    private Map<String, Object> buildClaimsFromAuthentication(String email, Long userId,
                                                              List<String> roles) {
        final Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("u_id", userId);
        claims.put("u_email", email);
        claims.put("u_roles", roles);
        return claims;
    }
}

