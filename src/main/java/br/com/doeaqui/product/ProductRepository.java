package br.com.doeaqui.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doeaqui.product.enums.DonationStatus;
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsBySubcategoryId(Long subcategoryId);
    
    Page<ProductEntity> findAllByStatus(DonationStatus status, Pageable pageable);

    Page<ProductEntity> findAllByDonorId(Long donorId, Pageable pageable);

    Page<ProductEntity> findAllByReceiverId(Long receiverId, Pageable pageable);
}
