package com.suraj.services.ProductMicroService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.suraj.services.ProductMicroService.core.data.ProductEntity;
import com.suraj.services.ProductMicroService.core.data.ProductsRepositry;
import com.suraj.services.ProductMicroService.core.events.ProductCreatedEvent;
import com.suraj.udemy.core.command.CancelReservedProductCommand;
import com.suraj.udemy.core.events.ProductReservationCancelledEvent;
import com.suraj.udemy.core.events.ProductReservedEvent;

@Component
@ProcessingGroup("products-group")
public class ProductEventsHandler {

	private final ProductsRepositry productsRepositry;

	public ProductEventsHandler(ProductsRepositry productsRepositry) {
		this.productsRepositry = productsRepositry;
	}

	/*
	 * This method can handle exception from all the event handler which throws same
	 * exception
	 */
	@ExceptionHandler
	public void handle(IllegalArgumentException exception) throws IllegalArgumentException {
		throw exception;
	}

	@ExceptionHandler
	public void handle(Exception exception) throws Exception{
		throw exception;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception {

		ProductEntity productEntity = new ProductEntity();

		BeanUtils.copyProperties(event, productEntity);

		productsRepositry.save(productEntity);
		
		//throw new Exception("Forcing exception after product entity is saved");
	}
	
	@EventHandler
	public void on(ProductReservedEvent reserveProductEvent) {
		ProductEntity productEntity = productsRepositry.findByProductId(reserveProductEvent.getProductId());
	    productEntity.setQuantity(productEntity.getQuantity() - reserveProductEvent.getQuantity());
	    
	    productsRepositry.save(productEntity);
	}
	
	@EventHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		
		ProductEntity currentProductEntity = productsRepositry.findByProductId(productReservationCancelledEvent.getProductId());
	    int quantity = currentProductEntity.getQuantity() + productReservationCancelledEvent.getQuantity();
		currentProductEntity.setQuantity(quantity);
	    
	    productsRepositry.save(currentProductEntity);
	}
}
