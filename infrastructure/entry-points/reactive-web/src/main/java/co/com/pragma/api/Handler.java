package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDto;
import co.com.pragma.api.mapper.UserDtoMapper;
import co.com.pragma.api.utils.Constants;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDtoMapper userDtoMapper;

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


}

