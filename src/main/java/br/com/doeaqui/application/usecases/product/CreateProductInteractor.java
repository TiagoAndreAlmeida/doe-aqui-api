package br.com.doeaqui.application.usecases.product;

import br.com.doeaqui.application.gateways.product.ProductGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class CreateProductInteractor {
    private ProductGateway productGateway;
    private UserGateway userGateway;

    public CreateProductInteractor(ProductGateway productGateway, UserGateway userGateway) {
        this.productGateway = productGateway;
        this.userGateway = userGateway;
    }

    public Product createProduct(Product product, Long donorId) {
        User donor = this.userGateway.findById(donorId)
        .orElseThrow(() -> new BusinessException("Usuário não encontrado", ErrorCode.NOT_FOUND));

        Product newProduct = new Product();
        newProduct.setTitle(product.getTitle());
        newProduct.setDescription(product.getDescription());
        newProduct.setCondition(product.getCondition());
        newProduct.setStatus(DonationStatus.AVAILABLE);
        newProduct.setDonor(donor);
        newProduct.setSubcategory(product.getSubcategory());

        return this.productGateway.createProduct(newProduct);
    }
}
