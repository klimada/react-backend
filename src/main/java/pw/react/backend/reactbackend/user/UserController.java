package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.reactbackend.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok().body(userRepository.findById(userId).orElseGet(User::new));
    }

    @PutMapping(path = "")
    public ResponseEntity<User> updateWholeUser(@RequestBody @Valid User updatedUser) {
        return ResponseEntity.ok().body(userRepository.save(updatedUser));
    }

    @PatchMapping(path = "")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updatedUser) {
        return ResponseEntity.ok().body(userService.updateUser(updatedUser));
    }
}
