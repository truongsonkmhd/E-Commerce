package vn.feature.service;

import vn.feature.dtos.CategoryDTO;
import vn.feature.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO category);

    Category getCategoryById(Long categoryId);

    List<Category> getAllCategories();

    Category updateCategory(Long categoryId, CategoryDTO category);

    void deleteCategory(Long categoryId);
}
