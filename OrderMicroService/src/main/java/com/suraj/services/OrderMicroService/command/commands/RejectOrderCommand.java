package com.suraj.services.OrderMicroService.command.commands;

import org.axonframework.modelling.command.AggregateIdentifier;

import lombok.Value;

@Value
public class RejectOrderCommand {
	@AggregateIdentifier
	private final String orderId;
	private final String reason;
}
