package br.com.doeaqui.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.doeaqui.product.exception.ProductNotFoundException;
import br.com.doeaqui.product.exception.ProductBusinessException;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;
import br.com.doeaqui.product.dto.request.CreateProductRequest;
import br.com.doeaqui.product.dto.response.ProductResponse;
import br.com.doeaqui.product.dto.response.ProductSummaryResponse;
import br.com.doeaqui.user.dto.response.UserSummaryResponse;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request, Long donorId) {
        UserEntity donor = userRepository.getReferenceById(donorId);

        ProductEntity product = new ProductEntity(
            null,
            request.title(),
            request.description(),
            request.condition(),
            DonationStatus.AVAILABLE,
            donor
        );

        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductSummaryResponse> listAvailable(Pageable pageable) {
        return productRepository.findAllByStatus(DonationStatus.AVAILABLE, pageable)
                .map(this::toSummaryResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado com o ID: " + id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse reserve(Long productId, Long receiverId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if (!product.getStatus().equals(DonationStatus.AVAILABLE)) {
            throw new ProductBusinessException("Produto não está disponível para reserva", HttpStatus.BAD_REQUEST);
        }

        if (product.getDonor().getId().equals(receiverId)) {
            throw new ProductBusinessException("O doador não pode reservar o próprio produto", HttpStatus.BAD_REQUEST);
        }

        UserEntity receiver = userRepository.getReferenceById(receiverId);
        product.setStatus(DonationStatus.RESERVED);
        product.setReceiver(receiver);

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse confirmDonation(Long productId, Long donorId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if (!product.getStatus().equals(DonationStatus.RESERVED)) {
            throw new ProductBusinessException("Produto precisa estar reservado para confirmar a doação", HttpStatus.BAD_REQUEST);
        }

        if (!product.getDonor().getId().equals(donorId)) {
            throw new ProductBusinessException("Apenas o doador pode confirmar a doação", HttpStatus.FORBIDDEN);
        }

        product.setStatus(DonationStatus.DONATED);

        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse cancelReservation(Long productId, Long userId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if (!product.getStatus().equals(DonationStatus.RESERVED)) {
            throw new ProductBusinessException("Produto não está reservado", HttpStatus.BAD_REQUEST);
        }

        boolean isDonor = product.getDonor().getId().equals(userId);
        boolean isReceiver = product.getReceiver().getId().equals(userId);

        if (!isDonor && !isReceiver) {
            throw new ProductBusinessException("Você não tem permissão para cancelar esta reserva", HttpStatus.FORBIDDEN);
        }

        product.setStatus(DonationStatus.AVAILABLE);
        product.setReceiver(null);

        return toResponse(productRepository.save(product));
    }

    private ProductResponse toResponse(ProductEntity product) {
        UserSummaryResponse donor = new UserSummaryResponse(
            product.getDonor().getName(),
            product.getDonor().getPhone()
        );

        return new ProductResponse(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getCondition(),
            product.getStatus(),
            donor,
            product.getCreatedAt()
        );
    }

    private ProductSummaryResponse toSummaryResponse(ProductEntity product) {
        return new ProductSummaryResponse(
            product.getId(),
            product.getTitle(),
            product.getCondition(),
            product.getStatus()
        );
    }
}
