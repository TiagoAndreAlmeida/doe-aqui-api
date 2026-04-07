package br.com.doeaqui.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private long id;
    private String name;
    private String slug;
    private List<Subcategory> subcategories = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    
    public Category() {
    }

    public Category(long id, String name, String slug, List<Subcategory> subcategories, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.subcategories = subcategories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
