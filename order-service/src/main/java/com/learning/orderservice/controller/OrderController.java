package com.learning.orderservice.controller;

import com.learning.orderservice.client.InventoryClient;
import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.model.Order;
import com.learning.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory;
    

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto){

        Resilience4JCircuitBreaker circuitBreaker = resilience4JCircuitBreakerFactory.create("inventory");
        Supplier<Boolean> booleanSupplier =()->orderDto.getOrderLineItemsList().stream().allMatch(orderLineItem -> inventoryClient.checkStock(orderLineItem.getSkuCode()));
        Boolean allProductInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErrorCase());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@  Order request received");


        if(allProductInStock){
            Order order=new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderLineItems(orderDto.getOrderLineItemsList());

            orderRepository.save(order);

            return "order place successfully";
        }else{
            return "order failed please try again";
        }

    }

    private Boolean handleErrorCase() {
        return false;
    }

    @GetMapping
    public String test(){
        return "API test";
    }

    @PostMapping("/test")
    public String testPost(){
        return "Post API is working";
    }
}
