package com.suraj.services.OrderMicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OrderMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMicroServiceApplication.class, args);
	}

}
