package hbv7d.controller;

import hbv7d.repository.UserRepository;
import hbv7d.model.User;

public class UserController {
    private UserRepository userRepository;
    

    public UserController(){
        
    }

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public boolean createUser(User user) {
        userRepository.save(user);
        return true;
    }
    

    public User getUser(int userId) {
        return userRepository.findById(userId);
    }
    
    public boolean deleteUser(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            userRepository.delete(userId);
            return true;
        }
        return false;
    }
}
