package com.example.ShoppingApp_BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.ShoppingApp_BackEnd.Data")
public class ShoppingAppBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingAppBackEndApplication.class, args);
	}

}
