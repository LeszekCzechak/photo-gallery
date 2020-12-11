package pl.czechak.leszek.photogalerybackend.controller.restController;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.czechak.leszek.photogalerybackend.configuration.security.EncryptorConfiguration;
import pl.czechak.leszek.photogalerybackend.configuration.security.SecurityConfiguration;
import pl.czechak.leszek.photogalerybackend.dto.CreateUserRequest;
import pl.czechak.leszek.photogalerybackend.dto.LoggedUser;
import pl.czechak.leszek.photogalerybackend.dto.UserResponse;
import pl.czechak.leszek.photogalerybackend.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {SecurityConfiguration.class, EncryptorConfiguration.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void ShouldPassToCreateUser() throws Exception {
        String userRequest = " {\n" +
                "    \"username\": \"user1\",\n" +
                "    \"password\": \"Testpassword\",\n" +
                "    \"admin\": false\n" +
                " }";

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userRequest))
                .andDo(print())
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateUserRequest> argumentCaptor = ArgumentCaptor.forClass(CreateUserRequest.class);
        Mockito.verify(userService).createUser(argumentCaptor.capture());
        CreateUserRequest argumentCaptorValue = argumentCaptor.getValue();

        assertThat(argumentCaptorValue.getUsername()).isEqualTo("user1");
        assertThat(argumentCaptorValue.getPassword()).isEqualTo("Testpassword");
        assertThat(argumentCaptorValue.getAdmin()).isFalse();
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldPassToDeleteUser() throws Exception {
        long userId = 73L;

        mockMvc.perform(delete("/user/delete/" + userId))
                .andExpect(status().isOk());
        Mockito.verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {
        //given

        long userId = 23L;
        String username = "TestUsername";
        UserResponse userResponse = new UserResponse(userId, username, null);
        List<UserResponse> list = new ArrayList<>(Arrays.asList(userResponse));

        when(userService.getAllUsers()).thenReturn(list);

        //when/then
        mockMvc.perform(get("/user/all"))
                .andDo(print())
                .andExpect(status().isOk());

//        [{ "userId":23, "username":"TestUsername", "galleries":null }]

    }

    @Test
    void shouldCheckLoginStatus() throws Exception {

        LoggedUser loggedUser= new LoggedUser("TestUsername");

        when(userService.checkLoginStatus()).thenReturn(loggedUser);

        mockMvc.perform(get("/user/check-login-status"))
                .andExpect(status().isOk());
    }
}
