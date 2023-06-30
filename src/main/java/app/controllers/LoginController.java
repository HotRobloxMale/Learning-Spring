package app.controllers;


import app.models.User;
import app.security.UserCredentials;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@EnableWebSecurity
@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final UserCredentials userCredentials;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserService userService, UserCredentials userCredentials, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userCredentials = userCredentials;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showLoginForm(Model model) {
            return "login";
    }


    @PostMapping("/process")
    public String processLogin(Model model, @RequestParam("login") String login, @RequestParam("password") String password, HttpServletRequest request) {
        User user = userService.getUserByLogin(login);
        if(user!=null){
        if(user.isConfirmed()){
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Please enter a password.");
            return "login";
        } else {
            if (userService.existsByLogin(login)) {
                String dbPassword = userService.getPasswordByLogin(login);
                System.out.println(dbPassword);
                System.out.println(password);
                if (passwordEncoder.matches(password, dbPassword)) {
                    if (user.isMaillogin()) {
                        userCredentials.setUsername(login);
                        userCredentials.setPassword(password);
                        System.out.println(userCredentials.getUsername());
                        System.out.println(userCredentials.getPassword());
                        return "redirect:/login/authenticate";
                    } else {
                        System.out.println("I'm here");
                        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login, password);

                        // Authenticate the user
                        Authentication auth = authenticationManager.authenticate(authReq);

                        // Set the authentication object in the security context
                        SecurityContext sc = SecurityContextHolder.getContext();
                        sc.setAuthentication(auth);

                        // Create a new session and add the security context.
                        HttpSession session = request.getSession(true);
                        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
                        System.out.println(auth.getAuthorities());
                        return "redirect:/home";
                    }
                }


            } else {
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }
        }
        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }
        model.addAttribute("error", "confirm your account at first");
        return "login";
    }
        model.addAttribute("error", "Invalid username or password.");
        return "login";
    }
}
