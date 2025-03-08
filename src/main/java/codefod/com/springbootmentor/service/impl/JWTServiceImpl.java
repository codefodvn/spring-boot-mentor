package codefod.com.springbootmentor.service.impl;

import codefod.com.springbootmentor.entity.User;
import codefod.com.springbootmentor.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        if (userDetails instanceof User user) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getUserId());
            claims.put("username", user.getUsername());
            claims.put("email", user.getEmail());
            claims.put("fullName", user.getFullname());
            claims.put("roles", user.getUserRoles().stream()
                    .map(userRole -> userRole.getRole().getName())
                    .collect(Collectors.toSet())); // Use Set to avoid duplicate roles

            String subject = user.getUsername() != null ? user.getUsername() : user.getEmail();

            return Jwts.builder().setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(
                            new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 168)) // 12 hours
                    .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                    .compact();
        } else {
            throw new IllegalArgumentException("UserDetails must be an instance of User");
        }
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            final String subject = extractUsername(token);
            return ((subject.equals(userDetails.getUsername()) || subject.equals(
                    userDetails.getUsername())) && !isTokenExpired(token));
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }
    }

    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            Authentication authentication
    ) {
        return buildToken(extraClaims, authentication, TimeUnit.MINUTES.toMillis(30));
    }


    private String buildToken(
            Map<String, Object> extraClaims,
            Authentication authentication,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
