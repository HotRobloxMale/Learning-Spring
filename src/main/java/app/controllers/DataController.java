package app.controllers;

import app.models.User;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/data")
public class DataController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showData(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String login = auth.getName(); // get logged in username
            String username = userService.getUsernameByLogin(login);
            boolean maillogin = userService.getEmailloginByLogin(login);
            // Add the username to the model
            model.addAttribute("username", username);
            model.addAttribute("login", login);
            model.addAttribute("maillogin", maillogin);
        }
        return "data";
    }

    @PostMapping("/savedata")
    public String saveData(Model model, @RequestParam(value = "maillogin", required = false, defaultValue = "false") Boolean mailLogin, @RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("cpassword") String cpassword){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Confirm Password: " + cpassword);
        System.out.println("Maillogin: " + mailLogin);
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String login = auth.getName(); // get logged in username
            if(password.equals("")) {
                userService.updateUsername(login, username);
                userService.updateMailLogin(login, mailLogin);
                model.addAttribute("message", "User data changed.");
                model.addAttribute("status", "success");
                return "/data";
            }
            else if(password.equals(cpassword) && !cpassword.equals("") ) {
                User user = userService.getUserByLogin(login);
                userService.updateUsername(login, username);
                userService.updateMailLogin(login, mailLogin);
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                userService.saveUser(user);
                model.addAttribute("message", "Password changed.");
                model.addAttribute("status", "success");
                return "/data";
            }
            else {
                model.addAttribute("message", "Passwords do not match.");
                model.addAttribute("status", "error");
                return "/data";
            }
        }
        else {
            model.addAttribute("message", "You are not logged in.");
            model.addAttribute("status", "error");
            return "/data";
        }
    }
}

