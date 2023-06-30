package app.controllers;

import app.models.Categories;
import app.models.SubCategories;
import app.services.CategoryService;
import app.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @Autowired
    public AdminController(CategoryService categoryService, SubCategoryService subCategoryService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }
    @GetMapping
    public String showadmin(Model model) {
        List<Categories> categories = categoryService.findAll();
        for (Categories category : categories) {
            List<SubCategories> subcategories = subCategoryService.getAllSubCategoriesByCategory(category);
            category.setSubcategories(subcategories);
        }
        model.addAttribute("categories", categories); // Add the attribute name "categories"
        return "admin";
    }

    @GetMapping("/addcategory")
    public String showaddcategory() {
        return "addcategory";
    }

    @GetMapping("/addsubcategory")
    public String showaddsubcategory(Model model) {
        List<Categories> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "addsubcategory";
    }



    @PostMapping("/savechanges")
    public String saveChanges(@RequestParam Map<String, String> requestParams) {
        List<Categories> categories = categoryService.findAll();

        for (Categories category : categories) {
            String categoryId = String.valueOf(category.getCategoryId());
            boolean categoryActive = requestParams.containsKey("categoryActive_" + categoryId);
            category.setActive(categoryActive);

            List<SubCategories> subcategories = subCategoryService.getAllSubCategoriesByCategory(category);
            for (SubCategories subcategory : subcategories) {
                String subcategoryId = String.valueOf(subcategory.getSubcategoryId());
                boolean subcategoryActive = requestParams.containsKey("subcategoryActive_" + subcategoryId);
                subcategory.setActive(subcategoryActive);
            }

            subCategoryService.saveAll(subcategories);
        }

        categoryService.saveAll(categories);

        return "redirect:/admin";
    }


    @PostMapping("/addcategory/add")
    public String addcategory(Model model, @RequestParam("categoryName") String categoryName,
                              @RequestParam("categoryDescription") String categoryDescription,
                              @RequestParam(value = "categoryVisible", required = false) Boolean categoryActive) {
        Categories category = new Categories();
        category.setCategoryName(categoryName);
        category.setCategoryDescription(categoryDescription);
        category.setActive(categoryActive != null ? categoryActive : false);
        categoryService.savecategory(category);
        model.addAttribute("message", "Category added succesfully.");
        model.addAttribute("status", "success");
        return "redirect:/admin/addcategory";
    }

    @PostMapping("/addsubcategory/add")
    public String addsubcategory(Model model,@RequestParam("subcategoryName") String subcategoryName,
    @RequestParam("subcategoryDescription") String subcategoryDescription,
     @RequestParam(value = "subcategoryVisible", required = false) Boolean subcategoryActive,
    @RequestParam("category") Long categoryId,
    @RequestParam("question1") String question1,
    @RequestParam("question2") String question2,
    @RequestParam("question3") String question3,
    @RequestParam("question4") String question4,
    @RequestParam("question5") String question5) {
        SubCategories subcategory = new SubCategories();
        subcategory.setSubcategoryName(subcategoryName);
        subcategory.setSubcategoryDescription(subcategoryDescription);
        subcategory.setActive(subcategoryActive != null ? subcategoryActive : false);
        subcategory.setCategory(categoryService.findCategoryById(categoryId));
        subcategory.setQuestion1(question1);
        subcategory.setQuestion2(question2);
        subcategory.setQuestion3(question3);
        subcategory.setQuestion4(question4);
        subcategory.setQuestion5(question5);
        subCategoryService.saveSubcategory(subcategory);
        model.addAttribute("message", "Subcategory added succesfully.");
        model.addAttribute("status", "success");
        return "redirect:/admin/addsubcategory";
    }
}
