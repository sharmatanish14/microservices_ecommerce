package com.learning.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
@CircuitBreaker(name="inventory")
public interface InventoryClient {

    @GetMapping("/api/inventory/{skucode}")
    Boolean checkStock(@PathVariable String skucode);
}
