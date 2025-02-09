package com.jwt.service.impl;

import com.jwt.model.User;
import com.jwt.repository.UserRepository;
import com.jwt.service.UserService;
import com.jwt.util.ExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(User newUser) {
        User oldUser = userRepository.findById(newUser.getId()).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
        if (newUser.getId() == oldUser.getId()){
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());
            userRepository.save(oldUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userRepository.findById(id).get();
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public String verify(User user) {
        Authentication authentication
                = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }

        return "Fail";
    }
}
