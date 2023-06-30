package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import app.models.User;
import app.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Boolean getEmailloginByLogin(String login){
        User user = userRepository.findByLogin(login);
        return user != null ? user.isMaillogin() : null;
    }

    public User getUserByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }
    public boolean isConfirmed(String login) {
        User user = userRepository.findByLogin(login);
        return user != null && user.isConfirmed();
    }
    public long getIdByLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user != null ? user.getUserId() : null;
    }
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public String getUsernameByLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user != null ? user.getUsername() : null;
    }
    public String getPasswordByLogin(String login){
        User user = userRepository.findByLogin(login);
        return user != null ? user.getPassword() : null;
    }
    public void updatePassword(String login, String password) {
        User user = userRepository.findByLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public void updatePasswordByConfirmationToken(String confirmationToken, String password) {
        User user = userRepository.findByConfirmationToken(confirmationToken);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public void updateConfirmationToken(String login, String confirmationToken) {
        User user = userRepository.findByLogin(login);
        user.setConfirmationToken(confirmationToken);
        userRepository.save(user);
    }
    public void updateUsername(String login, String username) {
        User user = userRepository.findByLogin(login);
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateMailLogin(String login, boolean mailLogin) {
        User user = userRepository.findByLogin(login);
        user.setMaillogin(mailLogin);
        userRepository.save(user);
    }
}
