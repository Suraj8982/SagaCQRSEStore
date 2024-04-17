package com.suraj.services.ProductMicroService.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.suraj.services.ProductMicroService.core.events.ProductCreatedEvent;
import com.suraj.udemy.core.command.CancelReservedProductCommand;
import com.suraj.udemy.core.command.ReserveProductCommand;
import com.suraj.udemy.core.events.ProductReservationCancelledEvent;
import com.suraj.udemy.core.events.ProductReservedEvent;


@Aggregate
public class ProductAggregate {
	@AggregateIdentifier
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;

	public ProductAggregate() {

	}

	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) {
		// Validation that can be added to validate Product details

		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();

		BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
		/*
		 * This method creates a event product created event in the event store once the
		 * whole transaction is committed which we can check on axon dashboard.
		 */
		AggregateLifecycle.apply(productCreatedEvent);

//		if(true) throw new Exception("Error took place in Product Command handler"); 
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.quantity = productCreatedEvent.getQuantity();
		this.title = productCreatedEvent.getTitle();
	}

	@CommandHandler
	public void handle(ReserveProductCommand reserveProductCommand) {
		
		if(quantity<reserveProductCommand.getQuantity()) {
			throw new IllegalArgumentException("Insufficient Number of product in stock");
		}
		ProductReservedEvent reserveProductEvent = ProductReservedEvent.builder()
				.orderId(reserveProductCommand.getOrderId())
				.productId(reserveProductCommand.getProductId())
				.userId(reserveProductCommand.getUserId())
				.quantity(reserveProductCommand.getQuantity()).build();
    
		AggregateLifecycle.apply(reserveProductEvent);
	}
	
	@EventSourcingHandler
	public void on(ProductReservedEvent reserveProductEvent) {
		this.quantity -= reserveProductEvent.getQuantity();
	}
	@CommandHandler
	public void handle(CancelReservedProductCommand cancelReservedProductCommand) {
		ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
				.productId(cancelReservedProductCommand.getProductId())
				.quantity(cancelReservedProductCommand.getQuantity())
				.orderId(cancelReservedProductCommand.getOrderId())
				.reason(cancelReservedProductCommand.getReason())
				.userId(cancelReservedProductCommand.getUserId())
				.build();

		AggregateLifecycle.apply(productReservationCancelledEvent);

	}
	
	@EventSourcingHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		this.quantity += productReservationCancelledEvent.getQuantity();
	}

}
