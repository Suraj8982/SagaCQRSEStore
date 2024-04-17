package com.suraj.services.ProductMicroService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsLookupRepositry extends JpaRepository<ProductLookupEntity, String> {

		ProductLookupEntity       findByProductIdOrTitle(String productId , String title);
}
