package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.DocumentAlreadyExists;
import co.com.pragma.usecase.user.exceptions.EmailAlreadyExists;
import co.com.pragma.usecase.user.exceptions.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository repository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(repository);
    }

    private User buildValidUser() {
        User user = new User();
        user.setName("Diego");
        user.setLastName("Gonzalez");
        user.setBirthday(LocalDate.parse("1999-01-01"));
        user.setAddress("Calle 123");
        user.setEmail("test@example.com");
        user.setDocumentId("12345678");
        user.setBaseSalary(200000);
        return user;
    }

    @Test
    void saveUser_successful() {
        User user = buildValidUser();

        when(repository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(repository.findByDocumentId(user.getDocumentId())).thenReturn(Mono.empty());
        when(repository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(repository).save(user);
    }

    @Test
    void saveUser_invalidData_shouldFail() {
        User invalidUser = new User();

        StepVerifier.create(userUseCase.saveUser(invalidUser))
                .expectError(InvalidDataException.class)
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void saveUser_emailAlreadyExists_shouldFail() {
        User user = buildValidUser();

        when(repository.findByEmail(user.getEmail()))
                .thenReturn(Mono.just(user));

        when(repository.findByDocumentId(user.getDocumentId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(EmailAlreadyExists.class)
                .verify();

        verify(repository, never()).save(any());
    }


    @Test
    void saveUser_documentAlreadyExists_shouldFail() {
        User user = buildValidUser();

        when(repository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(repository.findByDocumentId(user.getDocumentId()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectError(DocumentAlreadyExists.class)
                .verify();

        verify(repository, never()).save(any());
    }
}
