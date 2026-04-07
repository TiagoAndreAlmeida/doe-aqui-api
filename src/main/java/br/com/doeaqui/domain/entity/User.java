package br.com.doeaqui.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private Boolean inactive = false;
    private String email;
    private String name;
    private String password;
    private String phone = "";
    private List<Product> donatedProducts = new ArrayList<>();
    private List<Product> receivedProducts = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(Long id, Boolean inactive, String email, String name, String password, String phone,
            List<Product> donatedProducts, List<Product> receivedProducts, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.inactive = inactive;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.donatedProducts = donatedProducts;
        this.receivedProducts = receivedProducts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getDonatedProducts() {
        return donatedProducts;
    }

    public void setDonatedProducts(List<Product> donatedProducts) {
        this.donatedProducts = donatedProducts;
    }

    public List<Product> getReceivedProducts() {
        return receivedProducts;
    }

    public void setReceivedProducts(List<Product> receivedProducts) {
        this.receivedProducts = receivedProducts;
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
