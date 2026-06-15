package br.com.doeaqui.application.gateways.category;

import br.com.doeaqui.domain.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category createCategory(Category category);
    Optional<Category> findBySlug(String slug);
    List<Category> findAll();
    void deleteById(Long id);
    boolean existsBySlug(String slug);
}
