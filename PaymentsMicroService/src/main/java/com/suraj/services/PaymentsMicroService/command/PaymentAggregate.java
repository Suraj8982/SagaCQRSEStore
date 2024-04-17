package com.suraj.services.PaymentsMicroService.command;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.suraj.udemy.core.command.ProcessPaymentCommand;
import com.suraj.udemy.core.events.PaymentProcessedEvent;
import com.suraj.udemy.user.PaymentDetails;

@Aggregate
public class PaymentAggregate {
	
	@TargetAggregateIdentifier
	private  String paymentId;
	private  String orderId;

	public PaymentAggregate() {
		
	}
	public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
		
		if (processPaymentCommand.getPaymentDetails() == null || processPaymentCommand.getPaymentId() == null
				|| processPaymentCommand.getOrderId() == null) {
			throw new IllegalArgumentException();
		}
		
		PaymentProcessedEvent paymentProcessedEvent =   PaymentProcessedEvent.builder()
				                                       .orderId(processPaymentCommand.getOrderId())
				                                       .paymentId(processPaymentCommand.getPaymentId()).build();
		
		AggregateLifecycle.apply(paymentProcessedEvent);
		
	}
	
	@EventSourcingHandler
	private void on(PaymentProcessedEvent paymentProcessedEvent)
	{
		this.orderId = paymentProcessedEvent.getOrderId();
		this.paymentId = paymentProcessedEvent.getPaymentId();
	}
}
