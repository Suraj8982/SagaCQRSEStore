package com.suraj.services.ProductMicroService.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.suraj.services.ProductMicroService.command.CreateProductCommand;
import com.suraj.services.ProductMicroService.core.data.ProductLookupEntity;
import com.suraj.services.ProductMicroService.core.data.ProductsLookupRepositry;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<Message<?>>{

	
	private final ProductsLookupRepositry productsLookupRepositry;
	public CreateProductCommandInterceptor(ProductsLookupRepositry productsLookupRepositry) {
		 this.productsLookupRepositry = productsLookupRepositry;
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

	@Override
	public BiFunction<Integer, Message<?>, Message<?>> handle(List<? extends Message<?>> messages) {
		return (index, command) -> {

			LOGGER.info("Inside command:+" + command.getPayloadType());
			if (CreateProductCommand.class.equals(command.getPayloadType())) {
				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				ProductLookupEntity productLookupEntity = productsLookupRepositry.findByProductIdOrTitle(createProductCommand.getProductId(),
						createProductCommand.getTitle());

				if (productLookupEntity != null) {
					throw new IllegalStateException(
							String.format("Product with productId: %s and title: %s already exist",
									createProductCommand.getProductId(), createProductCommand.getTitle()));
				}
			}
			return command;
		};
	}


}
