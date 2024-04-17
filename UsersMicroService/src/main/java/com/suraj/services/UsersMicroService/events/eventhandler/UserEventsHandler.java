package com.suraj.services.UsersMicroService.events.eventhandler;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.suraj.udemy.core.query.FetchUserPaymentDetailsQuery;
import com.suraj.udemy.user.PaymentDetails;
import com.suraj.udemy.user.User;

@Component
public class UserEventsHandler {

	@QueryHandler
	public User handle(FetchUserPaymentDetailsQuery queryDetails) {
		PaymentDetails paymentDetails = PaymentDetails.builder()
				.cardNumber("123Card")
				.cvv("123")
				.name("SURAJ CHELANI")
				.validUntilMonth(12)
				.validUntilYear(2030).build();
		User user = User.builder()
				.userId(queryDetails.getUserId())
				.firstName("SURAJ")
				.lastName("CHELANI")
				.paymentDetails(paymentDetails).build();
		return user;
	}

}
