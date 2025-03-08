package codefod.com.springbootmentor.config.security.filter;

import codefod.com.springbootmentor.model.CredentialPayload;
import codefod.com.springbootmentor.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            final String jwtToken = authorizationHeader.substring(7);
            final String subject = jwtService.extractUsername(jwtToken);

            if (!StringUtils.isEmpty(subject)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                final Claims claims = jwtService.extractClaim(jwtToken, c -> c);

                final String tokenType = claims.get("token_type", String.class);
                if (!"access_token".equals(tokenType)) {
                    filterChain.doFilter(request, response);
                }

                final Long userId = claims.get("u_id", Long.class);
                final String email = claims.get("u_email", String.class);
                final Object roleObject = claims.get("u_roles", Object.class);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (!userDetails.isEnabled()) {
                    filterChain.doFilter(request, response);
                }

                List<SimpleGrantedAuthority> roles = new ArrayList<>();
                if (roleObject instanceof List<?>) {
                    roles = ((List<?>) roleObject).stream()
                            .map(item -> new SimpleGrantedAuthority(String.valueOf(item)))
                            .toList();
                }

                final CredentialPayload credentialPayload = CredentialPayload.builder()
                        .userId(userId)
                        .email(email)
                        .build();

                final Authentication auth = new UsernamePasswordAuthenticationToken(email,
                        credentialPayload, roles);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(request, response);
    }
}
