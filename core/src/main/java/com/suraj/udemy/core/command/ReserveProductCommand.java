package com.suraj.udemy.core.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReserveProductCommand {
	
	@TargetAggregateIdentifier
    private String productId;
    private int quantity;
    private String orderId;
    private String userId;
}
