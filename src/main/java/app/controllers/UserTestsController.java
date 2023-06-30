package app.controllers;

import app.models.Categories;
import app.models.SubCategories;
import app.models.UserTests;
import app.services.CategoryService;
import app.services.SubCategoryService;
import app.services.UserService;
import app.services.UserTestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Locale;
import java.util.List;

@Controller
@RequestMapping("/usertests")
public class UserTestsController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    private final UserService userService;
    private final UserTestsService userTestsService;

    @Autowired
    public UserTestsController(CategoryService categoryService, SubCategoryService subCategoryService, UserTestsService userTestsService, UserService userService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.userTestsService = userTestsService;
        this.userService = userService;
    }



    @GetMapping
    public String showUserTests(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Long userId = userService.getIdByLogin(login);
        model.addAttribute("locale", Locale.getDefault());
        if (userId != null) {
            System.out.println("User id: " + userId);
            List<UserTests> tests = userTestsService.getUserTestsByUserId(userId);
            model.addAttribute("userTests", tests);
        }
        return "usertests";
    }
}
