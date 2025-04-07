package poseidoncapitalsolutions.trading.controller;

import com.poseidoncapitalsolutions.trading.TradingApplication;
import com.poseidoncapitalsolutions.trading.model.User;
import com.poseidoncapitalsolutions.trading.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TradingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:insert_user_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", hasSize(2)));

        assertThat(userRepository.findAll())
                .extracting(User::getUsername)
                .containsExactly("user", "admin");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllUsersNotAdmin() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateUserOk() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(post("/user/validate")
                        .param("username", "newuser")
                        .param("rawPassword", "ValidPass123!")
                        .param("fullname", "New User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(initialCount + 1);
        assertThat(users.getLast().getUsername()).isEqualTo("newuser");
        assertThat(users.getLast().getFullname()).isEqualTo("New User");
        assertThat(passwordEncoder.matches("ValidPass123!", users.getLast().getPassword())).isTrue();
        assertThat(users.getLast().getRole()).isEqualTo("USER");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateUserInvalidData() throws Exception {
        // Test tous les cas d'erreur de validation
        mockMvc.perform(post("/user/validate")
                        .param("username", "")  // @NotBlank
                        .param("rawPassword", "short")  // @Size(min=8)
                        .param("fullname", "")  // @NotBlank
                        .param("role", ""))  // @NotBlank
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "username"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "rawPassword"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "fullname"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "role"));

        // Test spécifique pour le pattern du mot de passe
        mockMvc.perform(post("/user/validate")
                        .param("username", "valid")
                        .param("rawPassword", "invalidpassword")  // Manque majuscule/symbole
                        .param("fullname", "Valid Name")
                        .param("role", "USER"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "rawPassword"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormOk() throws Exception {
        User existingUser = userRepository.findAll().getFirst();

        mockMvc.perform(get("/user/update/" + existingUser.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/user/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateUserOk() throws Exception {
        int initialCount = userRepository.findAll().size();
        int id = userRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/user/update")
                        .param("id", String.valueOf(id))
                        .param("username", "updateduser")
                        .param("rawPassword", "ValidPass123!")
                        .param("fullname", "Updated User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertThat(userRepository.findAll()).hasSize(initialCount);
        User updatedUser = userRepository.findAll().getFirst();
        assertThat(updatedUser.getUsername()).isEqualTo("updateduser");
        assertThat(updatedUser.getFullname()).isEqualTo("Updated User");
        assertThat(passwordEncoder.matches("ValidPass123!", updatedUser.getPassword())).isTrue();
        assertThat(updatedUser.getRole()).isEqualTo("USER");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteUserOk() throws Exception {
        int initialCount = userRepository.findAll().size();
        User userToDelete = userRepository.findAll().getFirst();

        mockMvc.perform(get("/user/delete/" + userToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertThat(userRepository.findAll()).hasSize(initialCount - 1);
    }
}