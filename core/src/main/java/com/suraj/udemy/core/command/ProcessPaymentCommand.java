package com.suraj.udemy.core.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.suraj.udemy.user.PaymentDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessPaymentCommand {

	@TargetAggregateIdentifier
	private final String paymentId;
	private final String orderId;
	private final PaymentDetails paymentDetails;

}
