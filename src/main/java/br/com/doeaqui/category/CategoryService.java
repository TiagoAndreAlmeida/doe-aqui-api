package br.com.doeaqui.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.doeaqui.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;
import br.com.doeaqui.category.exception.CategoryNotEmptyException;
import br.com.doeaqui.category.exception.CategoryNotFoundException;
import br.com.doeaqui.category.exception.SlugAlreadyExistsException;
import br.com.doeaqui.category.mapper.CategoryMapper;
import br.com.doeaqui.util.SlugGenerator;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final SlugGenerator slugGenerator;

    public CategoryService(CategoryRepository categoryRepository, 
                           SubCategoryRepository subCategoryRepository, 
                           CategoryMapper categoryMapper,
                           SlugGenerator slugGenerator) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryMapper = categoryMapper;
        this.slugGenerator = slugGenerator;
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        String slug = slugGenerator.generate(request.name());

        if (categoryRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }

        CategoryEntity category = new CategoryEntity(request.name(), slug);
        CategoryEntity saved = categoryRepository.save(category);

        return categoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategorySummaryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(slug));
    }

    @Transactional
    public void delete(String slug) {
        CategoryEntity category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException(slug));

        // Regra de Integridade: Não permite excluir se houver subcategorias
        if (!subCategoryRepository.findByCategorySlug(slug).isEmpty()) {
            throw new CategoryNotEmptyException(category.getName());
        }

        categoryRepository.delete(category);
    }
}
