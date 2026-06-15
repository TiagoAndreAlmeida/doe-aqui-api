package br.com.doeaqui.infrastructure.controllers.category;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doeaqui.application.usecases.category.CreateCategoryInteractor;
import br.com.doeaqui.application.usecases.category.FindCategoryBySlugInteractor;
import br.com.doeaqui.application.usecases.category.ListCategoryInteractor;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.infrastructure.controllers.category.dto.CategoryDTOMapper;
import br.com.doeaqui.infrastructure.controllers.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategoryResponse;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategorySummaryResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CreateCategoryInteractor createCategoryInteractor;
    private ListCategoryInteractor listCategoryInteractor;
    private FindCategoryBySlugInteractor findCategoryBySlugInteractor;
    private CategoryDTOMapper categoryDTOMapper;

    public CategoryController(
        CreateCategoryInteractor createCategoryInteractor, CategoryDTOMapper categoryDTOMapper,
        ListCategoryInteractor listCategoryInteractor, FindCategoryBySlugInteractor findCategoryBySlugInteractor
    ) {
        this.createCategoryInteractor = createCategoryInteractor;
        this.categoryDTOMapper = categoryDTOMapper;
        this.listCategoryInteractor = listCategoryInteractor;
        this.findCategoryBySlugInteractor = findCategoryBySlugInteractor;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CreateCategoryRequest request) {
        Category categoryDomain = this.categoryDTOMapper.toDomain(request);
        categoryDomain = this.createCategoryInteractor.execute(categoryDomain);
        CategoryResponse response = this.categoryDTOMapper.toResponse(categoryDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    @GetMapping
    public ResponseEntity<List<CategorySummaryResponse>> findAll() {
        List<Category> categories = this.listCategoryInteractor.execute();
        List<CategorySummaryResponse> response = categories.stream().map(this.categoryDTOMapper::toSummaryResponse).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryResponse> findBySlug(@PathVariable String slug) {
        Category category = this.findCategoryBySlugInteractor.execute(slug);
        CategoryResponse response = this.categoryDTOMapper.toResponse(category);
        return ResponseEntity.ok(response);
    }

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
