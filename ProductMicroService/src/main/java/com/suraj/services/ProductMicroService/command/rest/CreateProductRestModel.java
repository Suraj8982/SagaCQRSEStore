package com.suraj.services.ProductMicroService.command.rest;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class CreateProductRestModel {
   // @NotBlank(message = "Title is a required field")
    private String title;
    @Min(value = 1 , message = "Price cannot be less than 1")
    private BigDecimal price;
    @Max(value = 5 , message = "quantity of article cannot be more than 5")
    @Min(value = 1 , message = "quantity of article cannot be less than 1")
    private Integer quantity;
}
