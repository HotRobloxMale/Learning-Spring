package app.controllers;

import app.models.User;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/passwordchange")
public class PChangeController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private String token1;

    @Autowired
    public PChangeController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping
    public String showPasswordChange(@RequestParam("token") String token) {
        User user = userService.getUserByConfirmationToken(token);
        if(user != null) {
          token1 = token;
           return "passwordchange";
       } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/process")
    public String processPasswordChange(Model model, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword) {
        User user = userService.getUserByConfirmationToken(token1);
        if(user != null) {
            if (password.equals("")) {
                model.addAttribute("errormessage", "Password cannot be empty");
                return "redirect:/passwordchange?token=" + token1;
            } else {
                if (password.equals(confirmPassword)) {
                    String encodedPassword = passwordEncoder.encode(password);
                    System.out.println(password);
                    System.out.println(encodedPassword);
                    System.out.println(token1);
                    user.setPassword(encodedPassword);
                    user.setConfirmationToken(null);
                    userService.saveUser(user);
                    return "login";
                } else {
                    model.addAttribute("errormessage", "Password cannot be empty");
                    return "redirect:/passwordchange?token=" + token1;
                }
            }
        }
        else {
            model.addAttribute("errormessage", "Invalid token");
            return "login";
        }
    }
}
