package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import app.models.User;
import app.services.UserService;

@Controller
public class ConfirmationController {

    private final UserService userService;

    @Autowired
    public ConfirmationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam("token") String token) {
        // Find the user by the confirmation token
        User user = userService.getUserByConfirmationToken(token);
        if (user != null) {
            // Mark the user as confirmed
            user.setConfirmed(true);
            user.setConfirmationToken(null);
            userService.saveUser(user);
            return "redirect:/login?confirmed";
        } else {
            return "redirect:/login?invalid";
        }
    }
}
