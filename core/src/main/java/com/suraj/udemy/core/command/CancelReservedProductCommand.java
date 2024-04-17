package com.suraj.udemy.core.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelReservedProductCommand {
	@TargetAggregateIdentifier
    private final  String productId;
    private final  String orderId;
    private final int quantity;
    private final String userId;
    private final String reason;

}
