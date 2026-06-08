package br.com.doeaqui.application.gateways.product;

import br.com.doeaqui.domain.entity.Product;

public interface ProductGateway {
    Product createProduct(Product product);
}
