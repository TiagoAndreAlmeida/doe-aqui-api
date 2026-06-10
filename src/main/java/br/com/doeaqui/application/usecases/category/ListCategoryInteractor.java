package br.com.doeaqui.application.usecases.category;

import java.util.List;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;

public class ListCategoryInteractor {
    private CategoryGateway categoryGateway;

    public ListCategoryInteractor(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    public List<Category> execute() {
        return this.categoryGateway.findAll();
    }

}
