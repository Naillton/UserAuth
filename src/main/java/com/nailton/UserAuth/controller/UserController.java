package com.nailton.UserAuth.controller;

import com.nailton.UserAuth.model.User;
import com.nailton.UserAuth.middleware.UserMiddleware;
import com.nailton.UserAuth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UserController implements UserMiddleware {

    @Autowired
    private UserService userService;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping(
            value = "/cadastro",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUser(@RequestBody User user) {
        ResponseEntity<String> valid = this.validCamps(user.getEmail(), user.getName(), user.getPassword());
        try {
            if (valid == null) {
                this.userService.insertUser(user);
                return ResponseEntity.status(HttpStatus.OK).body("Created/User");
            }
            return valid;
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> listUsers() {
        try {
            List<User> users = this.userService.listUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> listUserById(@PathVariable UUID id) {
        User user = this.userService.listUserById(id);
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User/NotFound");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User user) {
        User findUser = this.userService.listUserById(id);
        ResponseEntity<String> valid = this.validCamps(user.getEmail(), user.getName(), user.getPassword());
        try {
            if (findUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User/NotFound");
            } else {
                if (valid == null) {
                    findUser.setEmail(user.getEmail());
                    findUser.setName(user.getName());
                    findUser.setPassword(user.getPassword());
                    this.userService.updateUser(findUser);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(findUser);
                } else {
                    return valid;
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        User user = this.userService.listUserById(id);
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                this.userService.deleteUser(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("User/Deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteAllUsers() {
        try {
            this.userService.deleteAll();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("All/Users/Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> validCamps(String email, String name, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (name.length() < 3 || !matcher.matches() || password.length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid/Camps");
        }
        return null;
    }

}
