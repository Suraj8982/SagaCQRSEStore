package com.suraj.services.ProductMicroService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepositry extends JpaRepository<ProductEntity, String> {

		ProductEntity findByProductId(String productId);
		ProductEntity findByProductIdOrTitle(String productId , String title);
}
