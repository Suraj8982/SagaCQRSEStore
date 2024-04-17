package com.suraj.services.ProductMicroService.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suraj.services.ProductMicroService.command.CreateProductCommand;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

	private final Environment env;
	private final CommandGateway commandGateway;

	@Autowired
	public ProductCommandController(Environment env, CommandGateway commandGateway) {
		this.env = env;
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createProducts(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
		CreateProductCommand createProductCommand = CreateProductCommand.builder()
				.price(createProductRestModel.getPrice()).title(createProductRestModel.getTitle())
				.quantity(createProductRestModel.getQuantity()).productId(UUID.randomUUID().toString()).build();
		String response;

		response = commandGateway.sendAndWait(createProductCommand);

		return response;
	}

//	@GetMapping
//	public String getProducts() {
//		return "GET REQUEST... PORT" + env.getProperty("local.server.port");
//	}
//	
//	@PutMapping
	// return "PUT REQUEST...";
//	}
//	
//	@DeleteMapping
//	public String deleteProduct() {
//		return "DELETE REQUEST...";
//	}

}