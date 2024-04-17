package com.suraj.services.PaymentsMicroService.event.eventhandler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.suraj.services.PaymentsMicroService.core.data.PaymentEntity;
import com.suraj.services.PaymentsMicroService.core.data.PaymentsRepository;
import com.suraj.udemy.core.events.PaymentProcessedEvent;

@Component
public class PaymentEventsHandler {
	
	PaymentsRepository paymentsRepository; 

	public PaymentEventsHandler(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;		
	}
	@EventHandler
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		PaymentEntity paymentEntity = new PaymentEntity();
		BeanUtils.copyProperties(paymentProcessedEvent, paymentEntity);
		
		paymentsRepository.save(paymentEntity);
	}
}
