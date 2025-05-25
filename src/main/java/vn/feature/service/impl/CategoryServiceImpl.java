package vn.feature.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.feature.dtos.CategoryDTO;
import vn.feature.model.Category;
import vn.feature.repositorys.CategoryRepository;
import vn.feature.service.CategoryService;
import vn.feature.util.MessageKeys;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public Category createCategory(CategoryDTO category) {
        Category newCategory = Category.builder()
                .name(category.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).
                orElseThrow(()-> new RuntimeException(MessageKeys.NOT_FOUND));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category oldCategory =  getCategoryById(categoryId);
        oldCategory.setName(categoryDTO.getName());
        return categoryRepository.save(oldCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        // xoa cung trong db
        categoryRepository.deleteById(categoryId);
    }
}
