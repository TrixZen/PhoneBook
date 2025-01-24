package phonebook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phonebook.dto.LoginRequest;
import phonebook.model.User;
import phonebook.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(LoginRequest loginRequest) {
        String encodedPassword = passwordEncoder.encode(loginRequest.password()); // ✅ Нууц үгийг шифрлэх
        User user = new User(loginRequest.username(), encodedPassword);
        userRepository.save(user);
    }
}

