package app.repositories;

import app.models.Categories;
import app.models.SubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategories, Long> {
    List<SubCategories> findAllByCategory(Categories category);
    // Add other methods as needed

}
