package br.com.doeaqui.category;

import org.springframework.stereotype.Service;
import br.com.doeaqui.domain.service.SlugGenerator;
import br.com.doeaqui.infrastructure.controllers.category.dto.CategoryDTOMapper;
import br.com.doeaqui.infrastructure.persistence.category.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryDTOMapper categoryMapper;
    private final SlugGenerator slugGenerator;

    public CategoryService(CategoryRepository categoryRepository, 
                           SubCategoryRepository subCategoryRepository, 
                           CategoryDTOMapper categoryMapper,
                           SlugGenerator slugGenerator) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryMapper = categoryMapper;
        this.slugGenerator = slugGenerator;
    }

    // @Transactional(readOnly = true)
    // public CategoryResponse findBySlug(String slug) {
    //     return categoryRepository.findBySlug(slug)
    //             .map(categoryMapper::toResponse)
    //             .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));
    // }

    // @Transactional
    // public void delete(String slug) {
    //     CategoryEntity category = categoryRepository.findBySlug(slug)
    //             .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: " + slug, ErrorCode.NOT_FOUND));

    //     // Regra de Integridade: Não permite excluir se houver subcategorias
    //     if (!subCategoryRepository.findByCategorySlug(slug).isEmpty()) {
    //         throw new BusinessException("Não é possível excluir uma categoria que possui subcategorias vinculadas: " + category.getName(), ErrorCode.INVALID_STATE);
    //     }

    //     categoryRepository.delete(category);
    // }
}
