package com.suraj.services.ProductMicroService.core.data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductLookupEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8560687491194843695L;
	
	@Id
	@Column(unique = true)
	private  String productId;

	@Column(unique = true)
    private  String title;

}
