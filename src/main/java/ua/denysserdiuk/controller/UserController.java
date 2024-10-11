package ua.denysserdiuk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.denysserdiuk.model.User;
import ua.denysserdiuk.service.UserCreationService;

@RestController
public class UserController {
    private final UserCreationService userService;

    @Autowired
    UserController(UserCreationService userService){
    this.userService = userService;
    }

    @PostMapping("/CreateUser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String responseMessage = userService.createUser(user);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}
