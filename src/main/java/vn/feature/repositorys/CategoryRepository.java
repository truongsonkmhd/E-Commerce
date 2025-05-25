package vn.feature.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.feature.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
