package app.services;

import app.models.Categories;
import app.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> findAll() {
        return categoryRepository.findAll();
    }

    public List<Categories> saveAll(List<Categories> categories) {
        return categoryRepository.saveAll(categories);
    }
    public Categories savecategory(Categories category) {
        return categoryRepository.save(category);
    }

    public Categories findCategoryById(long l) {
        return categoryRepository.findCategoryByCategoryId(l);
    }
    // Add any additional methods you need here
}