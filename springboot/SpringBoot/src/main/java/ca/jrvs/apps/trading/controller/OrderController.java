package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.MarketOrder;
import ca.jrvs.apps.trading.model.SecurityOrder;
import ca.jrvs.apps.trading.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Soumettre un ordre de marché.
     */
    @PostMapping("/marketOrder")
    public ResponseEntity<SecurityOrder> postMarketOrder(@RequestBody MarketOrder orderData) {
        try {
            SecurityOrder securityOrder = orderService.executeMarketOrder(orderData);
            return new ResponseEntity<>(securityOrder, HttpStatus.CREATED);  // 201 CREATED
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 400 BAD REQUEST
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // 500 INTERNAL SERVER ERROR
        }
    }
}

