package co.com.pragma.model.authuser;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthUser {

    private String id;
    private String email;
    private String passwordHash;
    private Set<String> roles;

}
