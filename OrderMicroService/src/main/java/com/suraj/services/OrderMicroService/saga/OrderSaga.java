package com.suraj.services.OrderMicroService.saga;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.suraj.services.OrderMicroService.command.commands.ApproveOrderCommand;
import com.suraj.services.OrderMicroService.command.commands.RejectOrderCommand;
import com.suraj.services.OrderMicroService.core.events.OrderApprovedEvent;
import com.suraj.services.OrderMicroService.core.events.OrderCreatedEvent;
import com.suraj.services.OrderMicroService.core.events.OrderRejectedEvent;
import com.suraj.udemy.core.command.CancelReservedProductCommand;
import com.suraj.udemy.core.command.ProcessPaymentCommand;
import com.suraj.udemy.core.command.ReserveProductCommand;
import com.suraj.udemy.core.events.PaymentProcessedEvent;
import com.suraj.udemy.core.events.ProductReservationCancelledEvent;
import com.suraj.udemy.core.events.ProductReservedEvent;
import com.suraj.udemy.core.query.FetchUserPaymentDetailsQuery;
import com.suraj.udemy.user.User;

@Saga
public class OrderSaga {
	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	public static Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);

	@SagaEventHandler(associationProperty = "orderId")
	@StartSaga
	public void handle(OrderCreatedEvent orderCreatedEvent) {

		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.orderId(orderCreatedEvent.getOrderId()).productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity()).userId(orderCreatedEvent.getUserId()).build();

		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				// TODO Auto-generated method stub
				if (commandResultMessage.isExceptional()) {
					// write compensating transaction
				}
			}

		});
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		// payment details fetch

		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(
				productReservedEvent.getUserId());
		User userDetails = null;
		try {
			userDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();

		} catch (Exception e) {
			// process compensating transaction
			LOGGER.error("Process Compensating transaction as FetchUser payment failed."+e.getMessage());
			cancelproductReservation(productReservedEvent,e.getMessage());
			return;
		}
		if (userDetails == null) {
			LOGGER.error("Process Compensating transaction as FetchUser payment failed.");
			cancelproductReservation(productReservedEvent,"Could not fetch User payment details");
		}	
		LOGGER.info("FetchUser payment Success.");
		
		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
				.paymentId(UUID.randomUUID().toString())
				.orderId(productReservedEvent.getOrderId())
				.paymentDetails(userDetails.getPaymentDetails())
				.build();
		String result = null;
		try {
			result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
		} catch (Exception e) {
              //Compensating transaction
			cancelproductReservation(productReservedEvent,e.getMessage());
			return;
		}
		if(result == null) {
			//Compensating transaction as result is null
			cancelproductReservation(productReservedEvent,"Could not process payment");
		}
	}
	
	private void cancelproductReservation(ProductReservedEvent reserveProductEvent, String errorMessage) {
		CancelReservedProductCommand cancelReservedProductCommand = CancelReservedProductCommand.builder()
				.productId(reserveProductEvent.getProductId())
				.quantity(reserveProductEvent.getQuantity())
				.reason(errorMessage).build();

		commandGateway.send(cancelReservedProductCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		//Approve order  code
		ApproveOrderCommand approveOrderCommand =  new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		
		commandGateway.send(approveOrderCommand);
	}
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
		//Reject order command flow.
		RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),
				productReservationCancelledEvent.getReason());
		commandGateway.send(rejectOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		LOGGER.info("Order SAGA lifecyle is completeted for orderId: {} " ,orderApprovedEvent.getOrderId());
		//SagaLifecycle.end();
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		LOGGER.info("Order SAGA lifecyle is completeted for orderId: {} " ,orderRejectedEvent.getOrderId());
		//SagaLifecycle.end();
	}
}
