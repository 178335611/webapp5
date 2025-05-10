package com.itheima.ecommerceweb.Service;

import com.itheima.ecommerceweb.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) {
        Optional<User> existingUser = userRepository.findByName(user.getName());
        if (existingUser.isPresent()) {
            return false; // 用户名已存在
        }
        System.out.println("User " + user.getName() + " not exists");
        userRepository.save(user);
        return true;
    }

    public Optional<User> loginUser(String username, String password) {
        return userRepository.findByName(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public boolean deleteUser(String username) {
        Optional<User> user = userRepository.findByName(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
}