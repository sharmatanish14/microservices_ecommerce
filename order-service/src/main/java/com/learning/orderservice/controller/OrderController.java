package com.learning.orderservice.controller;

import com.learning.orderservice.client.InventoryClient;
import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.model.Order;
import com.learning.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto){

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@  Order request received");
        boolean allProductInStock = orderDto.getOrderLineItemsList().stream().allMatch(orderLineItem -> inventoryClient.checkStock(orderLineItem.getSkuCode()));

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

    @GetMapping
    public String test(){
        return "API test";
    }

    @PostMapping("/test")
    public String testPost(){
        return "Post API is working";
    }
}
