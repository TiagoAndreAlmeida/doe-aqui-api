package br.com.doeaqui.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.doeaqui.category.dto.request.CreateSubCategoryRequest;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import br.com.doeaqui.category.exception.CategoryNotFoundException;
import br.com.doeaqui.category.exception.SlugAlreadyExistsException;
import br.com.doeaqui.category.exception.SubCategoryNotEmptyException;
import br.com.doeaqui.category.exception.SubCategoryNotFoundException;
import br.com.doeaqui.category.mapper.SubCategoryMapper;
import br.com.doeaqui.product.ProductRepository;
import br.com.doeaqui.util.SlugGenerator;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SubCategoryMapper subCategoryMapper;
    private final SlugGenerator slugGenerator;

    public SubCategoryService(SubCategoryRepository subCategoryRepository,
                             CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             SubCategoryMapper subCategoryMapper,
                             SlugGenerator slugGenerator) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.subCategoryMapper = subCategoryMapper;
        this.slugGenerator = slugGenerator;
    }

    @Transactional
    public SubCategoryResponse create(CreateSubCategoryRequest request) {
        CategoryEntity category = categoryRepository.findBySlug(request.categorySlug())
                .orElseThrow(() -> new CategoryNotFoundException(request.categorySlug()));

        String slug = slugGenerator.generate(request.name());

        if (subCategoryRepository.existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }

        SubCategoryEntity subCategory = new SubCategoryEntity(request.name(), slug, category);
        SubCategoryEntity saved = subCategoryRepository.save(subCategory);

        return subCategoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SubCategoryResponse findBySlug(String slug) {
        return subCategoryRepository.findBySlug(slug)
                .map(subCategoryMapper::toResponse)
                .orElseThrow(() -> new SubCategoryNotFoundException(slug));
    }

    @Transactional(readOnly = true)
    public List<SubCategorySummaryResponse> findByCategorySlug(String categorySlug) {
        // Valida se a categoria existe
        if (!categoryRepository.existsBySlug(categorySlug)) {
            throw new CategoryNotFoundException(categorySlug);
        }

        return subCategoryRepository.findByCategorySlug(categorySlug)
                .stream()
                .map(subCategoryMapper::toSummaryResponse)
                .toList();
    }

    @Transactional
    public void delete(String slug) {
        SubCategoryEntity subCategory = subCategoryRepository.findBySlug(slug)
                .orElseThrow(() -> new SubCategoryNotFoundException(slug));

        // Regra de Integridade: Não permite excluir se houver produtos vinculados
        if (productRepository.existsBySubcategoryId(subCategory.getId())) {
            throw new SubCategoryNotEmptyException(subCategory.getName());
        }

        subCategoryRepository.delete(subCategory);
    }
}
