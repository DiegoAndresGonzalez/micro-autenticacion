package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "usuario")
public class UserEntity {

    @Id
    private Long id;
    private String name;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String email;
    private String password;
    @Column("document_id")
    private String documentId;
    private String phone;
    private Long roleId;
    private Integer baseSalary;

}

