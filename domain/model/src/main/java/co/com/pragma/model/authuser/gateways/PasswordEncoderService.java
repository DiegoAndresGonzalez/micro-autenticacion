package co.com.pragma.model.authuser.gateways;

public interface PasswordEncoderService {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);

}
