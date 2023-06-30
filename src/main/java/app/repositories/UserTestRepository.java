package app.repositories;

import app.models.Categories;
import app.models.SubCategories;
import app.models.UserTests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTestRepository extends JpaRepository<UserTests, Long> {
    List<UserTests> findAllByUserUserId(Long userId);

}
