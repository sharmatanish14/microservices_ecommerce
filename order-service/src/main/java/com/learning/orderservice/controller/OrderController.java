package com.learning.orderservice.controller;

import com.learning.orderservice.client.InventoryClient;
import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.model.Order;
import com.learning.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory;
    private final StreamBridge streamBridge;

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

            log.info("Sending order details to notification service : "+order.getId());
            streamBridge.send("notificationEventsSupplier-out-0",order.getId());
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
