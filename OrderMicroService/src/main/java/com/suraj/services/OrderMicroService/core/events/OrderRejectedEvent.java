package com.suraj.services.OrderMicroService.core.events;

import com.suraj.services.OrderMicroService.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderRejectedEvent {
	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
