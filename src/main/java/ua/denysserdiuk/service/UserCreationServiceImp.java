package ua.denysserdiuk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.denysserdiuk.model.User;
import ua.denysserdiuk.repository.UserRepository;

@Service
public class UserCreationServiceImp implements UserCreationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserCreationServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String createUser(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setName(user.getName());
        user1.setPassword(encodedPassword);

        userRepository.save(user1);
        return "User +" + user.getUsername() + " created";
    }
}

