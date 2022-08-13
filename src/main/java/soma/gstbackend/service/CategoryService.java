package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Category;
import soma.gstbackend.exception.CategoryException;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void join(Category category) {
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category findCategory(Long categoryId) throws Exception{
        Category category = categoryRepository.findOne(categoryId);
        if(category == null) {
            throw new CategoryException(ErrorCode.Category_Not_Found);
        }
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findCategories() { return categoryRepository.findAll(); }
}
