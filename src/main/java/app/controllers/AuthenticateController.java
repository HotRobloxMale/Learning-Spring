package app.controllers;

import app.models.User;
import app.repositories.UserRepository;
import app.security.UserCredentials;
import app.services.EmailService;
import app.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@EnableWebSecurity
@RequestMapping("/login/authenticate")
public class AuthenticateController {

    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime fifteenMinutesAgo = now.minusMinutes(15);
    private final UserCredentials userCredentials;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticateController(UserService userService, EmailService emailService, UserRepository userRepository, UserCredentials userCredentials, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userCredentials = userCredentials;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String authShow(RedirectAttributes redirectAttributes, Model model) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        if (username == null || password == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByLogin(username);

            if (userService.existsByLogin(username) && passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("User exists and password is correct");
                System.out.println(user.isMaillogin());

                if (user.isMaillogin()) {
                    if (user.getTokentime() == null) {
                        int generatedCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
                        String code = String.valueOf(generatedCode);
                        System.out.println("======================");
                        System.out.println(code);
                        user.setToken(code);
                        user.setTokentime(LocalDateTime.now());
                        userRepository.save(user);
                        emailService.sendEmail(user.getEmail(), "Authentication code", "Your authentication code is: " + code);
                    } else {
                        LocalDateTime dateTimeToCheck = user.getTokentime();
                        boolean isOlderThan15Minutes = dateTimeToCheck.isBefore(fifteenMinutesAgo); // Replace with your specific date and time
                        if (isOlderThan15Minutes && (user.getTokentime() != null)) {
                            int generatedCode = ThreadLocalRandom.current().nextInt(100000, 1000000);
                            String code = String.valueOf(generatedCode);
                            System.out.println("======================");
                            System.out.println(code);
                            user.setToken(code);
                            user.setTokentime(LocalDateTime.now());
                            userRepository.save(user);
                            emailService.sendEmail(user.getEmail(),"Authentication code", "Your authentication code is: " + code);

                            model.addAttribute("error", "Your code is no longer valid. Please refresh the page to get a new one.");
                            return "authenticate";
                        } else {
                            model.addAttribute("error", "You have already requested a code. Please wait 15 minutes before requesting a new one.");
                            return "authenticate";
                        }
                    }
                    return "authenticate";
                } else {
                    model.addAttribute("error", "Authentication disabled."); // Set the error message
                    return "login";
                }
            } else {
                model.addAttribute("error", "Invalid username or password."); // Set the error message
                return "login";
            }
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Invalid username or password."); // Set the error message
            return "login";
        }
    }


    @PostMapping
    public String authPost(@RequestParam("code") String code, Model model, HttpServletRequest request) {
        String login = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        System.out.println(password);
        System.out.println(login);
        User user = userService.getUserByLogin(login);

        if (user.isMaillogin()) {
            if (user.getTokentime() == null) {
                model.addAttribute("error", "You have not requested a code yet.");
                return "authenticate";
            } else {
                LocalDateTime dateTimeToCheck = user.getTokentime();
                boolean isOlderThan15Minutes = dateTimeToCheck.isBefore(fifteenMinutesAgo);
                if (isOlderThan15Minutes && (user.getTokentime() != null)) {
                    model.addAttribute("error", "Your code is no longer valid. Please refresh the page to get a new one.");
                    return "authenticate";
                } else {
                    if (user.getToken().equals(code)) {
                        user.setTokentime(null);
                        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login, password);

                        // Authenticate the user
                        Authentication auth = authenticationManager.authenticate(authReq);

                        // Set the authentication object in the security context
                        SecurityContext sc = SecurityContextHolder.getContext();
                        sc.setAuthentication(auth);

                        // Create a new session and add the security context.
                        HttpSession session = request.getSession(true);
                        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

                        return "redirect:/home";
                    } else {
                        model.addAttribute("error", "Invalid code.");
                        return "authenticate";
                    }
                }
            }
        } else {
            model.addAttribute("error", "Authentication disabled.");
            return "login";
        }
    }
}
