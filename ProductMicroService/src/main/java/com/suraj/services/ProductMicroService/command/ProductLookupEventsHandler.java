package com.suraj.services.ProductMicroService.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.suraj.services.ProductMicroService.core.data.ProductLookupEntity;
import com.suraj.services.ProductMicroService.core.data.ProductsLookupRepositry;
import com.suraj.services.ProductMicroService.core.events.ProductCreatedEvent;

@Component
@ProcessingGroup("products-group")
public class ProductLookupEventsHandler {

	private final ProductsLookupRepositry productsLookupRepositry;

	public ProductLookupEventsHandler(ProductsLookupRepositry productsLookupRepositry) {
		this.productsLookupRepositry = productsLookupRepositry;
	}
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		ProductLookupEntity productLookupEntity = new ProductLookupEntity(productCreatedEvent.getProductId(),
				productCreatedEvent.getTitle());
		
        productsLookupRepositry.save(productLookupEntity);
	}
}
