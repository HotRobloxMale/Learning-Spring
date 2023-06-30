package app.services;

import app.models.Categories;
import app.models.SubCategories;
import app.repositories.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<SubCategories> getAllSubCategoriesByCategory(Categories category) {
        return subCategoryRepository.findAllByCategory(category);
    }
    public List<SubCategories> saveAll(List<SubCategories> subCategories) {
        return subCategoryRepository.saveAll(subCategories);
    }

    public String getQuestion1ById(String id) {
        SubCategories subCategory = getSubcategory(id);
        return subCategory != null ? subCategory.getQuestion1() : null;
    }

    public String getQuestion2ById(String id) {
        SubCategories subCategory = getSubcategory(id);
        return subCategory != null ? subCategory.getQuestion2() : null;
    }

    public String getQuestion3ById(String id) {
        SubCategories subCategory = getSubcategory(id);
        return subCategory != null ? subCategory.getQuestion3() : null;
    }

    public String getQuestion4ById(String id) {
        SubCategories subCategory = getSubcategory(id);
        return subCategory != null ? subCategory.getQuestion4() : null;
    }

    public String getQuestion5ById(String id) {
        SubCategories subCategory = getSubcategory(id);
        return subCategory != null ? subCategory.getQuestion5() : null;
    }
    public void saveSubcategory(SubCategories subCategory) {
        subCategoryRepository.save(subCategory);
    }




    public SubCategories getSubcategory(String id) {
        // Convert the id to a Long value
        long subcategoryId;
        try {
            subcategoryId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            // The id is not a valid Long value
            return null;
        }
        Optional<SubCategories> optionalSubcategory = subCategoryRepository.findById(subcategoryId);

        return optionalSubcategory.orElse(null);
    }
}
