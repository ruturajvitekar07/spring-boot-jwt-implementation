package com.jwt.controller;

import com.jwt.exception.ResourceNotFoundException;
import com.jwt.model.User;
import com.jwt.model.response.JwtResponse;
import com.jwt.model.response.SuccessResponse;
import com.jwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user){
        User newUser = userService.addUser(user);
        if (newUser != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("User added successfully !!"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id){
        User user = userService.getUser(id);
        if (user != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found !!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()){
            throw new ResourceNotFoundException("Users list is empty !!");
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        boolean result = userService.deleteUser(id);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("User deleted successfully with ID : "+id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this ID : "+id);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User newUser){
        boolean result = userService.updateUser(newUser);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("User updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this ID : "+newUser.getId());
    }

    @PostMapping("/user/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }
}
