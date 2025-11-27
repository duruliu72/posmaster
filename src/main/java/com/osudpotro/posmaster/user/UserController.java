package com.osudpotro.posmaster.user;


import com.osudpotro.posmaster.category.DuplicateCategoryException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestHeader(required = false, name = "x-auth-token") String authToken, @RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        return userService.gerAllUsers();

    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegiterUserRequest request, UriComponentsBuilder uriBuilder) {
        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCategory(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Email is already exist.")
        );
    }
}
