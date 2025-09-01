package co.com.pragma.api.config;

import co.com.pragma.model.authuser.gateways.TokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class AuthorizationJwt implements WebFluxConfigurer {

    private final TokenService tokenService;
    private final ObjectMapper mapper;

    public AuthorizationJwt(TokenService tokenService,
                            ObjectMapper mapper,
                            @Value("${jwt.json-exp-roles}") String jsonExpRoles) {
        this.tokenService = tokenService;
        this.mapper = mapper;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/login").permitAll()
                        .pathMatchers("api/v1/usuarios/{documentId}").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitudes").hasRole("CLIENT")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(customJwtDecoder())
                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
                        )
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder customJwtDecoder() {

        return token -> {
            try {
                Map<String, Object> claims = tokenService.parseClaims(token);

                Object iatObj = claims.getOrDefault("iat", null);
                Object expObj = claims.getOrDefault("exp", null);

                Instant iat = (claims.containsKey("iat")) ? Instant.ofEpochSecond(((Number)claims.get("iat")).longValue()) : Instant.now();
                Instant exp = (claims.containsKey("exp")) ? Instant.ofEpochSecond(((Number)claims.get("exp")).longValue()) : Instant.now().plusSeconds(3600);

                Jwt jwt = new Jwt(token, iat, exp, Map.of("alg","HS256"), claims);
                return Mono.just(jwt);
            } catch (Exception e) {
                return Mono.error(new BadJwtException("Token inválido"));
            }
        };
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        var jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object roleClaim = jwt.getClaims().get("role");
            if (roleClaim == null) {
                return List.of();
            }

            String role = roleClaim.toString();
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }

    private List<String> getRoles(Map<String, Object> claims, String jsonExpClaim){
        List<String> roles = List.of();
        try {
            var json = mapper.writeValueAsString(claims);
            var chunk = mapper.readTree(json).at(jsonExpClaim);
            return mapper.readerFor(new TypeReference<List<String>>() {})
                    .readValue(chunk);
        } catch (IOException e) {
            log.error("Error extrayendo roles: {}", e.getMessage());
            return roles;
        }
    }
}
