package codefod.com.springbootmentor.service;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.Authentication;

public interface JwtService {

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(Authentication authentication);

    String generateToken(Map<String, Object> claims, Authentication authentication);

    String generateRefreshToken(Map<String, Object> claims, Authentication authentication);

    String generateToken(String subject, Map<String, Object> extraClaims);

    boolean isTokenValid(String token, Authentication authentication);

    boolean isTokenExpired(String token);
}