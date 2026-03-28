package br.com.doeaqui.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doeaqui.category.dto.request.CreateSubCategoryRequest;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/subcategories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public ResponseEntity<SubCategoryResponse> create(@RequestBody @Valid CreateSubCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subCategoryService.create(request));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<SubCategoryResponse> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(subCategoryService.findBySlug(slug));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        subCategoryService.delete(slug);
        return ResponseEntity.noContent().build();
    }
}
