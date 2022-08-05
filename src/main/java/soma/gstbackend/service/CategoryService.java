package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Category;
import soma.gstbackend.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }
}
