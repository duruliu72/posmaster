package com.osudpotro.posmaster.user;


import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping("/filter")
    public PagedResponse<UserMainDto> filterUsers(
            @RequestBody UserFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserMainDto> result = userService.filterUsers(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-or")
    public PagedResponse<UserMainDto> filterUsersOr(
            @RequestBody UserFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserMainDto> result = userService.filterForOrUsers(filter, pageable);
        return new PagedResponse<>(result);
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
