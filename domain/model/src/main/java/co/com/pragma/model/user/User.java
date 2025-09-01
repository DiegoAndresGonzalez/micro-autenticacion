package co.com.pragma.model.user;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;
    private String name;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String email;
    private String password;
    private String documentId;
    private String phone;
    private Long roleId;
    private Integer baseSalary;

}
