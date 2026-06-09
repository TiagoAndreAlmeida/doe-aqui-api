package br.com.doeaqui.infrastructure.controllers.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doeaqui.application.usecases.category.CreateCategoryInteractor;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.infrastructure.controllers.category.dto.CategoryDTOMapper;
import br.com.doeaqui.infrastructure.controllers.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategoryResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CreateCategoryInteractor createCategoryInteractor;
    private CategoryDTOMapper categoryDTOMapper;

    public CategoryController(CreateCategoryInteractor createCategoryInteractor, CategoryDTOMapper categoryDTOMapper) {
        this.createCategoryInteractor = createCategoryInteractor;
        this.categoryDTOMapper = categoryDTOMapper;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CreateCategoryRequest request) {
        Category categoryDomain = this.categoryDTOMapper.toDomain(request);
        categoryDomain = this.createCategoryInteractor.execute(categoryDomain);
        CategoryResponse response = this.categoryDTOMapper.toResponse(categoryDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    // @GetMapping
    // public ResponseEntity<List<CategorySummaryResponse>> findAll() {
    //     return ResponseEntity.ok(categoryService.findAll());
    // }

    // @GetMapping("/{slug}")
    // public ResponseEntity<CategoryResponse> findBySlug(@PathVariable String slug) {
    //     return ResponseEntity.ok(categoryService.findBySlug(slug));
    // }

    // @GetMapping("/{slug}/subcategories")
    // public ResponseEntity<List<SubCategorySummaryResponse>> findSubCategories(@PathVariable String slug) {
    //     return ResponseEntity.ok(subCategoryService.findByCategorySlug(slug));
    // }

    // @DeleteMapping("/{slug}")
    // public ResponseEntity<Void> delete(@PathVariable String slug) {
    //     categoryService.delete(slug);
    //     return ResponseEntity.noContent().build();
    // }
}
