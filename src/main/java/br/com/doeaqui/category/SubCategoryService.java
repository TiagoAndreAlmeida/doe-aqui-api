package br.com.doeaqui.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.doeaqui.category.dto.request.CreateSubCategoryRequest;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import br.com.doeaqui.category.mapper.SubCategoryMapper;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.infrastructure.persistence.product.ProductRepository;
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
                .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: " + request.categorySlug(), ErrorCode.NOT_FOUND));

        String slug = slugGenerator.generate(request.name());

        if (subCategoryRepository.existsBySlug(slug)) {
            throw new BusinessException("Já existe uma subcategoria com o slug: " + slug, ErrorCode.ALREADY_EXISTS);
        }

        SubCategoryEntity subCategory = new SubCategoryEntity(request.name(), slug, category);
        SubCategoryEntity saved = subCategoryRepository.save(subCategory);

        return subCategoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SubCategoryResponse findBySlug(String slug) {
        return subCategoryRepository.findBySlug(slug)
                .map(subCategoryMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Subcategoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<SubCategorySummaryResponse> findByCategorySlug(String categorySlug) {
        // Valida se a categoria existe
        if (!categoryRepository.existsBySlug(categorySlug)) {
            throw new BusinessException("Categoria não encontrada com o slug: " + categorySlug, ErrorCode.NOT_FOUND);
        }

        return subCategoryRepository.findByCategorySlug(categorySlug)
                .stream()
                .map(subCategoryMapper::toSummaryResponse)
                .toList();
    }

    @Transactional
    public void delete(String slug) {
        SubCategoryEntity subCategory = subCategoryRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException("Subcategoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));

        // Regra de Integridade: Não permite excluir se houver produtos vinculados
        if (productRepository.existsBySubcategoryId(subCategory.getId())) {
            throw new BusinessException("Não é possível excluir uma subcategoria que possui produtos vinculados: " + subCategory.getName(), ErrorCode.INVALID_STATE);
        }

        subCategoryRepository.delete(subCategory);
    }
}
