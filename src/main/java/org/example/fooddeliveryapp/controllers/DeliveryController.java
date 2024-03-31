package org.example.fooddeliveryapp.controllers;

import org.example.fooddeliveryapp.services.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    /**
     * Acquires necessary parameters from an HTTP request and returns a fee based of them.
     * @param city the location where the delivery is taking place
     * @param vehicle  type of vehicle that is used to deliver the food
     * @return how much the delivery costs
     */
    @GetMapping("/fee")
    public ResponseEntity<Double> getFee(@RequestParam String city, @RequestParam String vehicle) {
        double fee = deliveryService.calculateFee(city, vehicle);
        return new ResponseEntity<>(fee,HttpStatus.OK);


    }


}
