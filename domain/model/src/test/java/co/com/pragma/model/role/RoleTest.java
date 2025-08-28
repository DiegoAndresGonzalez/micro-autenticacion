package co.com.pragma.model.role;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void shouldBuildRoleCorrectly() {
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .description("Administrador del sistema")
                .build();

        assertThat(role.getName()).isEqualTo("ADMIN");
        assertThat(role.getDescription()).isEqualTo("Administrador del sistema");
    }

    @Test
    void shouldUseSettersAndGetters() {
        Role role = new Role();
        role.setName("USER");
        role.setDescription("Usuario básico");

        assertThat(role.getName()).isEqualTo("USER");
        assertThat(role.getDescription()).isEqualTo("Usuario básico");
    }
}
