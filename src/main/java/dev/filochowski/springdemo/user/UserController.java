package dev.filochowski.springdemo.user;

import dev.filochowski.springdemo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<CollectionModel<User>> getUsers() {
        List<User> users = userService.getUsers();

        for (User user : users) {
            Link selfLink = linkTo(UserController.class).slash(user.getId()).withSelfRel();
            user.add(selfLink);
        }

        Link selfLink = linkTo(UserController.class).withSelfRel();
        CollectionModel<User> userCollectionModel = CollectionModel.of(users, selfLink);

        return ResponseEntity.ok().body(userCollectionModel);
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long id) {
        Optional<User> optionalUser = userService.getUser(id);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException();
        }

        User user = optionalUser.get();
        Link selfLink = linkTo(UserController.class).slash(user.getId()).withSelfRel();
        user.add(selfLink);

        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") Long id) {
        boolean isPresent = userService.deleteUser(id);

        if (!isPresent) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable("userId") Long id, @RequestBody User request) {
        Optional<User> user = userService.updateUser(id, request);

        if (user.isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}