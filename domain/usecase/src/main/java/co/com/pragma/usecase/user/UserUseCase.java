package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exceptions.DocumentAlreadyExists;
import co.com.pragma.usecase.user.exceptions.EmailAlreadyExists;
import co.com.pragma.usecase.user.exceptions.InvalidDataException;
import co.com.pragma.usecase.user.utils.Constants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository repository;

    public Mono<User> saveUser(User user) {
        return validateUserData(user)
                .then(Mono.defer(() -> repository.save(user)));
    }

    public Mono<Void> validateUserData(User user) {
        if (user == null || user.getName() == null || user.getName().isBlank() ||
                user.getLastName() == null || user.getLastName().isBlank() ||
                user.getBirthday() == null ||
                user.getAddress() == null || user.getAddress().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank() ||
                !isValidEmail(user.getEmail()) ||
                user.getDocumentId() == null || !isValidDocument(user.getDocumentId()) ||
                user.getBaseSalary() == null || user.getBaseSalary() <= Constants.NUMBER_ZERO ||
                user.getBaseSalary() > Constants.MAX_SALARY) {

            return Mono.error(new InvalidDataException(Constants.INVALID_USER_DATA));
        }
        Mono<Void> emailValidation = validateExistingEmail(user.getEmail());
        Mono<Void> documentValidation = validateExistingDocument(user.getDocumentId());
        user.setRoleId(Constants.ID_TWO);
        return Mono.when(emailValidation, documentValidation);
    }

    private boolean isValidEmail(String email) {
        return Constants.EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidDocument(String documentId) {
        return Constants.DOCUMENT_PATTERN.matcher(documentId).matches();
    }

    private Mono<Void> validateExistingEmail(String email) {
        return repository.findByEmail(email)
                .handle((user, sink) -> sink.error
                        (new EmailAlreadyExists(Constants.EMAIL_EXISTS)))
                .switchIfEmpty(Mono.empty())
                .then();
    }

    private Mono<Void> validateExistingDocument(String documentId) {
        return repository.findByDocumentId(documentId)
                .handle((user, sink) -> sink.error
                        (new DocumentAlreadyExists(Constants.DOCUMENT_EXISTS)))
                .switchIfEmpty(Mono.empty())
                .then();
    }


}
