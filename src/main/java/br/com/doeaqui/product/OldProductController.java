package br.com.doeaqui.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.doeaqui.infrastructure.controllers.product.dto.response.ProductResponse;
import br.com.doeaqui.infrastructure.controllers.product.dto.response.ProductSummaryResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

@RestController
@RequestMapping("/Oldproducts")
public class OldProductController {

    private final ProductService productService;

    public OldProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> listAvailable(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<ProductSummaryResponse> products = productService.listAvailable(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/{id}/reserve")
    public ResponseEntity<ProductResponse> reserve(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        ProductResponse response = productService.reserve(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ProductResponse> confirm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        ProductResponse response = productService.confirmDonation(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ProductResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        ProductResponse response = productService.cancelReservation(id, user.getId());
        return ResponseEntity.ok(response);
    }
}
