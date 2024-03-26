package dev.filochowski.springdemo.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String email);
    List<User> getUsers();
    boolean deleteUser(Long id);
    Optional<User> updateUser(Long id, User user);
}
