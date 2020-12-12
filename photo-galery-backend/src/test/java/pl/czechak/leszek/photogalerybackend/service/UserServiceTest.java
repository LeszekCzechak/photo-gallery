package pl.czechak.leszek.photogalerybackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.czechak.leszek.photogalerybackend.dto.CreateUserRequest;
import pl.czechak.leszek.photogalerybackend.dto.LoggedUser;
import pl.czechak.leszek.photogalerybackend.dto.UserResponse;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GalleryRepository galleryRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(galleryRepository, userRepository, passwordEncoder);
    }


    @Test
    void shouldReturnUserDetailsByUsername() {
        //given
        String username = "UserTest";
        String password = "password";
        UserEntity userEntity = new UserEntity(1L, username, password, Set.of(UserRole.USER, UserRole.ADMIN), null);

        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userEntity));

        //when
        UserDetails userDetails = userService.loadUserByUsername(username);
        //then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    void shouldCreateUserWithAdminRole() {
        //given
        CreateUserRequest userRequest = new CreateUserRequest();

        String username = "UserTest";
        String password = "password";

        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setAdmin(true);
        //when
        userService.createUser(userRequest);
        //then
        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        UserEntity entity = argumentCaptor.getValue();
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getRoles()).contains(UserRole.ADMIN);
        assertThat(entity.getRoles()).contains(UserRole.USER);
    }

    @Test
    void shouldCreateUserWithoutAdminRole() {
        //given
        CreateUserRequest userRequest = new CreateUserRequest();

        String username = "UserTest";
        String password = "password";

        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setAdmin(false);
        //when
        userService.createUser(userRequest);
        //then
        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).save(argumentCaptor.capture());
        UserEntity entity = argumentCaptor.getValue();
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getRoles()).containsOnly(UserRole.USER);
        assertThat(entity.getRoles()).doesNotContain(UserRole.ADMIN);
    }

    @Test
    void shouldDeleteUser() {

        // given
        Long userId = 999L;
        String username = "UserTest";
        UserEntity userEntity = new UserEntity(userId, username, "password", Set.of(UserRole.USER, UserRole.ADMIN), null);
        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);

        when(userRepository.findById(userId)).thenReturn(optionalUserEntity);

        //when
        userService.deleteUser(userId);

        //then
        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).delete(argumentCaptor.capture());
        UserEntity entity = argumentCaptor.getValue();
        assertThat(entity.getUsername()).isEqualTo(username);
        assertThat(entity.getId()).isEqualTo(userId);

    }

    @Test
    void shouldReturnListOfUserResponse() {

        //given

        //when
        List<UserResponse> userResponseList = userService.getAllUsers();
        //then
        //TODO: fix this
    }

    @Test
    void shouldReturnLoggedUser() {

        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
          when(authentication.getName()).thenReturn("UserTest");

        //when
        LoggedUser loggedUser = userService.checkLoginStatus();
        //then
        assertThat(loggedUser.getUsername()).isEqualTo("UserTest");
    }
}
