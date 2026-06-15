package br.com.doeaqui.infrastructure.gateways.category;

import java.util.List;
import java.util.Optional;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.infrastructure.persistence.category.CategoryEntity;
import br.com.doeaqui.infrastructure.persistence.category.CategoryRepository;

public class CategoryRepositoryGateway implements CategoryGateway {
    private CategoryEntityMapper categoryEntityMapper;
    private CategoryRepository categoryRepository;

    public CategoryRepositoryGateway(CategoryEntityMapper categoryEntityMapper, CategoryRepository categoryRepository) {
        this.categoryEntityMapper = categoryEntityMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        CategoryEntity entity = this.categoryEntityMapper.toEntity(category);
        CategoryEntity saved = this.categoryRepository.save(entity);
        return this.categoryEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return this.categoryRepository.findBySlug(slug)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        List<CategoryEntity> entities = this.categoryRepository.findAll();
        return entities.stream().map(this.categoryEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }

}
