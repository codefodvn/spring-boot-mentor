package codefod.com.springbootmentor.service;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    <T> T extractClaim(String token, Function<Claims, T> claimsResolvers);

    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    boolean isValidToken(String token, UserDetails userDetails);
    String generateToken(Map<String, Object> claims, Authentication authentication);
}
