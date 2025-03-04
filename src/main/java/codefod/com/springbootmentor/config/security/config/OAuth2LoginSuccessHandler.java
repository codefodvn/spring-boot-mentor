package codefod.com.springbootmentor.config.security.config;

import codefod.com.springbootmentor.constant.ClaimConstant;
import codefod.com.springbootmentor.model.CredentialPayload;
import codefod.com.springbootmentor.model.LoginResponse;
import codefod.com.springbootmentor.model.User;
import codefod.com.springbootmentor.service.JwtService;
import codefod.com.springbootmentor.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();

            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            userService.findByEmail(email).ifPresentOrElse(user -> {
                user.setSource("GITHUB");
                userService.save(user);
            }, () -> {
                User user = new User();
                user.setRole("ROLE_USER");
                user.setEmail(email);
                user.setName(name);
                user.setSource("GITHUB");
                userService.save(user);
            });
        }
        final LoginResponse loginResponse = new LoginResponse();

        List<String> roles = List.of("USER");

        Map<String, Object> claims = new LinkedHashMap<>();
        if (authentication.getCredentials() instanceof CredentialPayload credentialPayload) {
            claims = buildClaimsFromAuthentication(credentialPayload.getUsername(),
                    credentialPayload.getUserId(), roles);
        }
        claims.put(ClaimConstant.TOKEN_TYPE, ClaimConstant.ACCESS_TOKEN);
        loginResponse.setAccessToken(jwtService.generateToken(claims, authentication));

        claims.put(ClaimConstant.TOKEN_TYPE, ClaimConstant.REFRESH_TOKEN);
        loginResponse.setRefreshToken(jwtService.generateRefreshToken(claims, authentication));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }

    private Map<String, Object> buildClaimsFromAuthentication(String username, Long userId,
                                                              List<String> roles) {
        final Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(ClaimConstant.AUTH_USER_ID, userId);
        claims.put(ClaimConstant.AUTH_USER_NAME, username);
        claims.put(ClaimConstant.AUTH_USER_ROLES, roles);
        claims.put(ClaimConstant.TOKEN_TYPE, ClaimConstant.ACCESS_TOKEN);
        return claims;
    }
}