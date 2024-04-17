package com.suraj.services.ProductMicroService.query.rest;

import java.math.BigDecimal;

import lombok.Data;

/* created this class so that if we need to send only 
 * selected fields in response so we can do that
 * */
@Data
public class ProductRestModel {
	
	private  String productId;
    private  String title;
    private  BigDecimal price;
    private  Integer amount;
}
