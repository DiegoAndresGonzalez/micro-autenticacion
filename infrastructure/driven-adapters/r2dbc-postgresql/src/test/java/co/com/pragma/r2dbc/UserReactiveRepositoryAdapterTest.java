package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private User buildValidUser() {
        return User.builder()
                .id(1L)
                .name("Diego")
                .email("diego@test.com")
                .build();
    }

    private UserEntity buildValidUserEntity(){
        return UserEntity.builder()
                .id(1L)
                .name("Diego")
                .email("diego@test.com")
                .build();
    }

    @Test
    void mustFindValueById() {
        User user = buildValidUser();
        UserEntity userEntity = buildValidUserEntity();
        when(repository.findById(1L)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        User user = buildValidUser();
        UserEntity userEntity = buildValidUserEntity();
        when(repository.findAll()).thenReturn(Flux.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("diego@test.com"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        User user = buildValidUser();
        UserEntity userEntity = buildValidUserEntity();

        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(userEntity));
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findByExample(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getName().equals("Diego"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        User user = User.builder().id(1L).name("Diego").email("diego@test.com").build();
        UserEntity userEntity = UserEntity.builder().id(1L).name("Diego").email("diego@test.com").build();

        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));

        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void mustFindByEmail() {
        User user = buildValidUser();
        UserEntity userEntity = buildValidUserEntity();

        when(repository.findByEmail("diego@test.com")).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByEmail("diego@test.com");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("diego@test.com"))
                .verifyComplete();
    }

    @Test
    void mustFindByDocumentId() {
        User user = buildValidUser();
        UserEntity userEntity = buildValidUserEntity();

        when(repository.findByDocumentId("123456")).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByDocumentId("123456");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(1L))
                .verifyComplete();
    }

}
