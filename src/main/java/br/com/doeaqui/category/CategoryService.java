package br.com.doeaqui.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.doeaqui.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;
import br.com.doeaqui.category.mapper.CategoryMapper;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
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
            throw new BusinessException("Já existe uma categoria com o slug: " + slug, ErrorCode.ALREADY_EXISTS);
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
                .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void delete(String slug) {
        CategoryEntity category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));

        // Regra de Integridade: Não permite excluir se houver subcategorias
        if (!subCategoryRepository.findByCategorySlug(slug).isEmpty()) {
            throw new BusinessException("Não é possível excluir uma categoria que possui subcategorias vinculadas: " + category.getName(), ErrorCode.INVALID_STATE);
        }

        categoryRepository.delete(category);
    }
}
