package com.suraj.services.ProductMicroService.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.suraj.services.ProductMicroService.core.data.ProductEntity;
import com.suraj.services.ProductMicroService.core.data.ProductsRepositry;
import com.suraj.services.ProductMicroService.query.rest.ProductRestModel;

@Component
public class ProductsQueryHandler {

	private final ProductsRepositry productsRepositry;

	public ProductsQueryHandler(ProductsRepositry productsRepositry) {
		this.productsRepositry = productsRepositry;
	}
	
	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery query) {
		List<ProductRestModel> productRest = new ArrayList<>();
		List<ProductEntity> storedProducts = productsRepositry.findAll();
		for (ProductEntity entity : storedProducts) {
			ProductRestModel productRestModel = new ProductRestModel();
			BeanUtils.copyProperties(entity, productRestModel);
			productRest.add(productRestModel);
		}
		return productRest;
	}
}
