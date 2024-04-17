package com.suraj.services.PaymentsMicroService.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paymentDetails")
public class PaymentEntity {
	@Id
	private String paymentId;
	@Column
	public String orderId;

}
