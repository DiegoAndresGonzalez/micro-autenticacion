package co.com.pragma.model.role.gateways;

import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<String> findNameById(Long id);
}
