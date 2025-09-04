package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDto;
import co.com.pragma.api.dto.LoginRequestDto;
import co.com.pragma.api.dto.LoginResponseDto;
import co.com.pragma.api.mapper.UserDtoMapper;
import co.com.pragma.api.utils.Constants;
import co.com.pragma.usecase.auth.AuthUseCase;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userDtoMapper;
    private final AuthUseCase authUseCase;

    public Mono<ServerResponse> createUser(ServerRequest request){
        log.info(Constants.LOG_ACCOUNT_REQUEST);
        return request.bodyToMono(CreateUserDto.class)
                .map(userDtoMapper::toModel)
                .flatMap(userUseCase::saveUser)
                .doOnNext(user -> log.info(Constants.LOG_SUCCESSFUL_REQUEST))
                .doOnError(user -> log.error(Constants.LOG_ERROR_HANDLER))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDtoMapper.toResponse(user)));
    }

    public Mono<ServerResponse> findByDocumentId(ServerRequest request) {
        String documentId = request.pathVariable("documentId");
        log.info(Constants.LOG_SEARCH_USER, documentId);
        return userUseCase.findUserByDocumentId(documentId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(e -> log.error(Constants.LOG_ERROR_HANDLER))
                .doOnSuccess(s -> log.info(Constants.USER_FOUND, documentId));

    }


    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequestDto.class)
                .flatMap(loginRequest ->
                        authUseCase.authenticate(loginRequest.email(), loginRequest.password())
                )
                .flatMap(responseDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(responseDto)
                );
    }

    public Mono<ServerResponse> createAdvisor(ServerRequest request){
        log.info(Constants.LOG_ACCOUNT_REQUEST);
        return request.bodyToMono(CreateUserDto.class)
                .map(userDtoMapper::toModel)
                .flatMap(userUseCase::createAdvisor)
                .doOnNext(user -> log.info(Constants.LOG_SUCCESSFUL_REQUEST))
                .doOnError(user -> log.error(Constants.LOG_ERROR_HANDLER))
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDtoMapper.toResponse(user)));
    }

    public Mono<ServerResponse> findByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        log.info("Buscando usuario por email {}", email);

        return userUseCase.findByEmail(email)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(e -> log.error(Constants.LOG_ERROR_HANDLER))
                .doOnSuccess(s -> log.info("Usuario encontrado con email {}", email));
    }


}

