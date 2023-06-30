package app.controllers;

import app.models.Categories;
import app.models.SubCategories;
import app.services.CategoryService;
import app.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @Autowired
    public CategoryController(CategoryService categoryService, SubCategoryService subCategoryService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }
    @GetMapping
    public String showAvailableCategories(Model model) {
        List<Categories> categories = categoryService.findAll();
        for (Categories category : categories) {
            List<SubCategories> subcategories = subCategoryService.getAllSubCategoriesByCategory(category);
            category.setSubcategories(subcategories);
        }
        model.addAttribute("categories", categories);
        return "category";
    }
}
