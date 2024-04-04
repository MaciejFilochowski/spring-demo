package dev.filochowski.springdemo.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUser(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUser(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        userRepository.delete(optionalUser.get());
        return true;
    }

    @Override
    public Optional<User> updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User updatedUser = optionalUser.get();
        if (user.getFirstname() != null) {
            updatedUser.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null) {
            updatedUser.setLastname(user.getLastname());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }

        return Optional.of(userRepository.save(updatedUser));
    }
}
