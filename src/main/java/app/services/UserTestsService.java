package app.services;

import app.models.UserTests;
import app.repositories.UserTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTestsService {
    private UserTestRepository userTestRepository;
    private UserService userService;

    @Autowired
    public UserTestsService(UserTestRepository userTestRepository, UserService userService) {
        this.userTestRepository = userTestRepository;
        this.userService = userService;
    }

    public void saveUserTest(UserTests userTest) {
        userTestRepository.save(userTest);
    }

    public List<UserTests> getUserTestsByUserId(Long userId) {
        return userTestRepository.findAllByUserUserId(userId);
    }
}
