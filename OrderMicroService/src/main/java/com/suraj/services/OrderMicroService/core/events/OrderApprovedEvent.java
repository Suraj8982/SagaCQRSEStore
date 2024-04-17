package com.suraj.services.OrderMicroService.core.events;

import com.suraj.services.OrderMicroService.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
