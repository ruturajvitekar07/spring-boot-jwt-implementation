package com.jwt.service;

import com.jwt.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getUser(int id);
    List<User> getAllUsers();
    User addUser(User user);
    boolean updateUser(User newUser);
    boolean deleteUser(int id);
    String verify(User user);
}
