package com.suraj.services.ProductMicroService;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

import com.suraj.services.ProductMicroService.command.interceptors.CreateProductCommandInterceptor;
import com.suraj.services.ProductMicroService.core.errorhandling.ProductEventErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMicroServiceApplication.class, args);
	}
    
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context , CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("products-group", conf -> new ProductEventErrorHandler());
	//	config.registerListenerInvocationErrorHandler("products-group", conf -> PropagatingErrorHandler.instance());

	}


}
