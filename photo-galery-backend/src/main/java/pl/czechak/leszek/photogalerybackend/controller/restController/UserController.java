package pl.czechak.leszek.photogalerybackend.controller.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czechak.leszek.photogalerybackend.dto.CreateUserRequest;
import pl.czechak.leszek.photogalerybackend.dto.LoggedUser;
import pl.czechak.leszek.photogalerybackend.dto.UserResponse;
import pl.czechak.leszek.photogalerybackend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest newUser) {
        userService.createUser(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/check-login-status")
    public LoggedUser authenticate() {
        return userService.checkLoginStatus();
    }


}
