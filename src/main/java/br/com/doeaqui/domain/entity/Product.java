package br.com.doeaqui.domain.entity;

import java.time.LocalDateTime;

import br.com.doeaqui.category.SubCategoryEntity;
import br.com.doeaqui.product.enums.ConditionStatus;
import br.com.doeaqui.product.enums.DonationStatus;
import br.com.doeaqui.user.UserEntity;

public class Product {
    private Long id;
    private String title;
    private String description;
    private ConditionStatus condition;
    private DonationStatus status;
    private UserEntity donor;
    private UserEntity receiver;
    private SubCategoryEntity subcategory;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(Long id, String title, String description, ConditionStatus condition, DonationStatus status,
            UserEntity donor, UserEntity receiver, SubCategoryEntity subcategory, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.status = status;
        this.donor = donor;
        this.receiver = receiver;
        this.subcategory = subcategory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConditionStatus getCondition() {
        return condition;
    }

    public void setCondition(ConditionStatus condition) {
        this.condition = condition;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public UserEntity getDonor() {
        return donor;
    }

    public void setDonor(UserEntity donor) {
        this.donor = donor;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public SubCategoryEntity getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategoryEntity subcategory) {
        this.subcategory = subcategory;
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
