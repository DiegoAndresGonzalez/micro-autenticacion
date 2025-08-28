package co.com.pragma.model.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldBuildUserCorrectly() {
        LocalDate birthday = LocalDate.of(1995, 5, 20);

        User user = User.builder()
                .id(1L)
                .name("Diego")
                .lastName("González")
                .birthday(birthday)
                .address("Calle 123")
                .email("diego@test.com")
                .documentId("123456789")
                .phone("3001234567")
                .roleId(2L)
                .baseSalary(3000000)
                .build();

        assertThat(user.getName()).isEqualTo("Diego");
        assertThat(user.getLastName()).isEqualTo("González");
        assertThat(user.getBirthday()).isEqualTo(birthday);
        assertThat(user.getBaseSalary()).isEqualTo(3000000);
    }

    @Test
    void shouldUseSettersAndGetters() {
        User user = new User();
        user.setName("Carlos");
        user.setEmail("carlos@test.com");

        assertThat(user.getName()).isEqualTo("Carlos");
        assertThat(user.getEmail()).isEqualTo("carlos@test.com");
    }
}
