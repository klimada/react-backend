package pw.react.backend.reactbackend.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.reactbackend.exceptions.ResourceNotFoundException;
import pw.react.backend.reactbackend.user.User;
import pw.react.backend.reactbackend.user.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException(String.format("User with id [%s] not found.", user.getId()));
    }
}
