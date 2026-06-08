package br.com.doeaqui.infrastructure.controllers.product.dto;

import org.springframework.stereotype.Component;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.infrastructure.controllers.product.dto.request.CreateProductRequest;
import br.com.doeaqui.infrastructure.controllers.product.dto.response.ProductResponse;
import br.com.doeaqui.infrastructure.controllers.user.dto.response.UserSummaryResponse;

@Component
public class ProductDTOMapper {

    public Product toDomain(CreateProductRequest request) {
        Product product = new Product();
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setCondition(request.condition());
        return product;
    }

    public ProductResponse toResponse(Product product) {
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
}
