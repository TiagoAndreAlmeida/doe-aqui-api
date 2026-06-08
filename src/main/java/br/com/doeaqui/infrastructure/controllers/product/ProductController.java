package br.com.doeaqui.infrastructure.controllers.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doeaqui.application.usecases.product.CreateProductInteractor;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.infrastructure.controllers.product.dto.ProductDTOMapper;
import br.com.doeaqui.infrastructure.controllers.product.dto.request.CreateProductRequest;
import br.com.doeaqui.infrastructure.controllers.product.dto.response.ProductResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final CreateProductInteractor createProductInteractor;
    private final ProductDTOMapper productDTOMapper;

    public ProductController(CreateProductInteractor createProductInteractor, ProductDTOMapper productDTOMapper) {
        this.createProductInteractor = createProductInteractor;
        this.productDTOMapper = productDTOMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request, @AuthenticationPrincipal UserEntity user) {
        Product productDomain = productDTOMapper.toDomain(request);
        Product createdProduct = createProductInteractor.createProduct(productDomain, user.getId());
        ProductResponse response = productDTOMapper.toResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
