package co.com.pragma.model.authuser;

import co.com.pragma.model.user.User;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long expiresIn;
}
