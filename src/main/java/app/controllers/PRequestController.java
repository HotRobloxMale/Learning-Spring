package app.controllers;

import app.repositories.UserRepository;
import app.services.EmailService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/passwordrequest")
public class PRequestController {
    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userrepository;


    @Autowired
    public PRequestController(UserRepository userrepository, EmailService emailService, UserService userService) {
        this.userrepository = userrepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping
    public String showPasswordRequest() {
            return "passwordrequest";
    }

    @PostMapping("/process")
    public String processPasswordRequest(Model model, @RequestParam("email") String email) {

        String login = userrepository.findByEmail(email).getLogin();
        if (userService.isConfirmed(login)) {
            if (userrepository.existsByEmail(email)) {
                String confirmationToken = UUID.randomUUID().toString();
                model.addAttribute("success", "Email sent");
                userService.updateConfirmationToken(login, confirmationToken);
                String confirmationLink = "http://localhost:8080/passwordchange?token=" + confirmationToken;
                emailService.sendEmail(email, "proceed to password change", confirmationLink);
                return "redirect:/";
            } else {
                model.addAttribute("errormessage", "Email not found");
            }
            return "passwordrequest";
        }
        model.addAttribute("errormessage", "Account not confirmed");
        return "passwordrequest";
    }

}
