package br.com.doeaqui.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.doeaqui.product.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean inactive = false;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone = "";

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> donatedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<ProductEntity> receivedProducts = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserEntity(
        Long id, String name, String email, String phone, String password,
        Boolean inactive
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.inactive = inactive;
        this.password = password;
    }

    protected UserEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInativo(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ProductEntity> getDonatedProducts() {
        return donatedProducts;
    }

    public void setDonatedProducts(List<ProductEntity> donatedProducts) {
        this.donatedProducts = donatedProducts;
    }

    public List<ProductEntity> getReceivedProducts() {
        return receivedProducts;
    }

    public void setReceivedProducts(List<ProductEntity> receivedProducts) {
        this.receivedProducts = receivedProducts;
    }
}
