package com.suraj.services.ProductMicroService.core.data;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Getter
@Table(name="products")
public class ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8911069819992262659L;
    
	@Id
	@Column(unique = true)
	private  String productId;

	@Column(unique = true)
    private  String title;
    private  BigDecimal price;
    private  Integer quantity;

}
