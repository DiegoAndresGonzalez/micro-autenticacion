package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDto;
import co.com.pragma.api.dto.UserResponseDto;
import co.com.pragma.api.mapper.UserDtoMapper;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RouterRestTest {

    private UserUseCase userUseCase;
    private UserDtoMapper userDtoMapper;
    private WebTestClient webTestClient;

    private User buildUser() {
        return User.builder()
                .id(1L)
                .name("Diego")
                .lastName("Prueba")
                .birthday(LocalDate.of(1995, 1, 1))
                .address("Calle 123")
                .email("correo@test.com")
                .documentId("123456")
                .phone("3001234567")
                .roleId(2L)
                .baseSalary(2000)
                .build();
    }

    private CreateUserDto buildCreateUserDto() {
        return new CreateUserDto(
                "Diego",
                "Prueba",
                LocalDate.of(1995, 1, 1),
                "Calle 123",
                "correo@test.com",
                "123456",
                "3001234567",
                2000
        );
    }

    private UserResponseDto buildUserDto() {
        return new UserResponseDto(
                1L,
                "Diego",
                "correo@test.com",
                LocalDate.of(1995, 1, 1),
                "Calle 123",
                "correo@test.com",
                "123456",
                "3001234567",
                2L,
                2000

        );
    }

    @BeforeEach
    void setUp() {

        userUseCase = Mockito.mock(UserUseCase.class);
        userDtoMapper = Mockito.mock(UserDtoMapper.class);

        Handler handler = new Handler(userUseCase, userDtoMapper);

        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(handler)).build();
    }

    @Test
    void createUser_success() {

        User model = buildUser();
        CreateUserDto requestDto = buildCreateUserDto();
        UserResponseDto responseDto = buildUserDto();

        when(userDtoMapper.toModel(any(CreateUserDto.class))).thenReturn(model);
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(model));
        when(userDtoMapper.toResponse(any(User.class))).thenReturn(responseDto);

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.name").isEqualTo("Diego")
                .jsonPath("$.email").isEqualTo("correo@test.com");
    }
}
