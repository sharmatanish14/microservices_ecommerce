package com.learning.inventryservice.controller;

import com.learning.inventryservice.model.Inventory;
import com.learning.inventryservice.repository.InventoryRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @GetMapping("/{skucode}")
    Boolean isInStock(@PathVariable String skucode){
        Inventory inventory = inventoryRepository.findBySkuCode(skucode)
                .orElseThrow(() -> new RuntimeException("Can not find product by skucode :" + skucode));

        return inventory.getStock()>0;
    }

}
