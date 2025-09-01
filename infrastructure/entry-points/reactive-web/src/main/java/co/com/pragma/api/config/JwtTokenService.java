package co.com.pragma.api.config;

import co.com.pragma.model.authuser.gateways.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenService implements TokenService {

    private final Key secretKey;

    public JwtTokenService(@Value("${security.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generate(Map<String, Object> claims, Duration ttl) {
        Instant now = Instant.now();
        String issuer = "pragma-auth";

        return Jwts.builder()
                .issuer(issuer)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Map<String, Object> parseClaims(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

}