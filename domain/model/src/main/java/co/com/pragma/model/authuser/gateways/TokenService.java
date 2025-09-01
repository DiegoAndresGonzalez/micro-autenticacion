package co.com.pragma.model.authuser.gateways;

import java.time.Duration;
import java.util.Map;

public interface TokenService {

    String generate(Map<String, Object> claims, Duration ttl);
    Map<String, Object> parseClaims(String token);

}
