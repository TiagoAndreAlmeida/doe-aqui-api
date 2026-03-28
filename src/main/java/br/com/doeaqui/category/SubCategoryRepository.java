package br.com.doeaqui.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {
    Optional<SubCategoryEntity> findBySlug(String slug);
    List<SubCategoryEntity> findByCategorySlug(String categorySlug);
    boolean existsBySlug(String slug);
}
