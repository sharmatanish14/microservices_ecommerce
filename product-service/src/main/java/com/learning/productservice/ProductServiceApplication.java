package com.learning.productservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
public class ProductServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
