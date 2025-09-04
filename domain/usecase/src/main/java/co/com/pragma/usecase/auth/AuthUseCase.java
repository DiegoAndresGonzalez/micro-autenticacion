package co.com.pragma.usecase.auth;


import co.com.pragma.model.authuser.AuthResponse;
import co.com.pragma.model.authuser.gateways.PasswordEncoderService;
import co.com.pragma.model.authuser.gateways.TokenService;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.auth.exceptions.InvalidCredentialsException;
import co.com.pragma.usecase.user.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoder;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public Mono<AuthResponse> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Usuario no encontrado")))
                .flatMap(user -> {
                    boolean ok = passwordEncoder.matches(rawPassword, user.getPassword());
                    if (!ok) return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));

                    return roleRepository.findNameById(user.getRoleId())
                            .map(roleName -> {
                                Map<String, Object> claims = Map.of(
                                        "sub", user.getEmail(),
                                        "role", roleName
                                );
                                String token = tokenService.generate(claims, Duration.ofHours(1));
                                return new AuthResponse(token, System.currentTimeMillis() + 3600000);
                            });
                });
    }
}

