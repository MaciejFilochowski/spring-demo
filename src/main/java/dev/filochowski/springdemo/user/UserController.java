package dev.filochowski.springdemo.user;

import dev.filochowski.springdemo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") Long id) {
        boolean isPresent = userService.deleteUser(id);

        if (!isPresent) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long id, @RequestBody User request) {
        Optional<User> user = userService.updateUser(id, request);

        if (user.isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(user.get(), HttpStatus.NO_CONTENT);
    }
}