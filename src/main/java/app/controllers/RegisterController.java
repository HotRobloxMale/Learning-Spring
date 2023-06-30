package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.models.User;
import app.services.EmailService;
import app.services.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserService userService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (user.getUsername().length() < 3 || user.getUsername().length() > 20) {
            bindingResult.rejectValue("username", "error.user", "Username must be between 3 and 20 characters.");
            return "register";
        }

        if (user.getLogin().length() < 3 || user.getLogin().length() > 20) {
            bindingResult.rejectValue("login", "error.user", "Login must be between 3 and 20 characters.");
            return "register";
        }

        if (user.getPassword().length() < 3) {
            bindingResult.rejectValue("password", "error.user", "Password must be at least 3 characters.");
            return "register";
        }
        if (userService.existsByLogin(user.getLogin())) {
            redirectAttributes.addFlashAttribute("exists", true);
            redirectAttributes.addFlashAttribute("message", "User already exists. Please choose a different login.");
            return "redirect:/register";
        }
        if(userService.existsByEmail(user.getEmail())){
            redirectAttributes.addFlashAttribute("exists", true);
            redirectAttributes.addFlashAttribute("message", "User already exists. Please choose a different mail.");
            return "redirect:/register";
        }
        else {
            String confirmationToken = UUID.randomUUID().toString();
            user.setConfirmationToken(confirmationToken);
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userService.saveUser(user);

            String confirmationLink = "http://localhost:8080/confirm?token=" + confirmationToken;
            emailService.sendEmail(user.getEmail(), "Confirm your registration", confirmationLink);

            return "redirect:/register/success";
        }
    }

    @GetMapping("/success")
    public String registrationSuccess() {
        return "/success";
    }
}
