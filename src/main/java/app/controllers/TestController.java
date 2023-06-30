package app.controllers;

import app.models.SubCategories;
import app.models.UserTests;
import app.repositories.UserTestRepository;
import app.services.GptAnswerService;
import app.services.SubCategoryService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/test")
public class TestController {


   private final UserTestRepository userTestRepository;
   private final SubCategoryService subcategoryService;
   private final UserService userService;
   private final GptAnswerService gptAnswerService;

   @Autowired
    public TestController(UserTestRepository userTestRepository, SubCategoryService subcategoryService, UserService userService, GptAnswerService gptAnswerService) {
       this.userTestRepository = userTestRepository;
       this.subcategoryService = subcategoryService;
       this.userService = userService;
       this.gptAnswerService = gptAnswerService;
   }
    private String savedId;

    @GetMapping
    public String test(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            SubCategories subcategory = subcategoryService.getSubcategory(id);
            model.addAttribute("subcategory", subcategory);
            savedId = id;
            return "test";
        } else {
            return "category";
        }
    }
    @PostMapping("/process")
    public String results(Model model,@RequestParam("answer1") String answer1,@RequestParam("answer2") String answer2,
                          @RequestParam("answer3") String answer3,@RequestParam("answer4") String answer4,
                          @RequestParam("answer5") String answer5) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
        if (savedId != null) {
            SubCategories subcategory = subcategoryService.getSubcategory(savedId);
            model.addAttribute("subcategory", subcategory);
            UserTests userTest = new UserTests();
            answer1 = answer1.replace("\n", " ");
            answer2 = answer2.replace("\n", " ");
            answer3 = answer3.replace("\n", " ");
            answer4 = answer4.replace("\n", " ");
            answer5 = answer5.replace("\n", " ");
            userTest.setAnswer1(answer1);
            userTest.setAnswer2(answer2);
            userTest.setAnswer3(answer3);
            userTest.setAnswer4(answer4);
            userTest.setAnswer5(answer5);
            userTest.setSubCategory(subcategory);
            String q1 = subcategoryService.getQuestion1ById(savedId);
            String q2 = subcategoryService.getQuestion2ById(savedId);
            String q3 = subcategoryService.getQuestion3ById(savedId);
            String q4 = subcategoryService.getQuestion4ById(savedId);
            String q5 = subcategoryService.getQuestion5ById(savedId);

            System.out.println(q1);
            System.out.println(q2);
            System.out.println(q3);
            System.out.println(q4);
            System.out.println(q5);

            System.out.println(answer1);
            System.out.println(answer2);
            System.out.println(answer3);
            System.out.println(answer4);
            System.out.println(answer5);

            String login = auth.getName(); // get logged in username
            userTest.setUser(userService.getUserByLogin(login));
            String message="\"User provided the following answers to five questions, some of which contain certain language code: "+
                    " 1. Question: "+q1+". Answer: "+answer1+
                    "2. Question: "+q2+". Answer: "+answer2+
                    "3. Question: "+q3+". Answer: "+answer3+
                    "4. Question: "+q4+". Answer: "+answer4+
                    "5. Question: "+q5+". Answer: "+answer5+
                    "Please assess the correctness of the user's answers, including the validity of any Python code, and suggest areas for further learning.";
            try {
                System.out.println(message);
                String jsonResponse = gptAnswerService.callOpenAI(message);
                System.out.println(jsonResponse);
                String summary = gptAnswerService.extractTextFromResponse(jsonResponse);
                System.out.println(summary);
                userTest.setSummary(summary);
                userTest.setTesttime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                userTestRepository.save(userTest);
                return "redirect:/category";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "category";
        } else {
            return "category";
        }
        } else {
            return "login";
        }
    }
}
