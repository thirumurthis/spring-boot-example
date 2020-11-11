package com.learn.sb.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.sb.model.Order;
import com.learn.sb.model.OrderStatus;
import com.learn.sb.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;

	@GetMapping("/{id}")
	public Order getOrder(@PathVariable(name="id") int id) {
		
		return orderService.getOrders(id);
	}
	
	@PostMapping("addOrder")
	public OrderStatus addOrder(@RequestBody Order order) {
		return orderService.addOrder(order);
	}
	
	@GetMapping("orders")
	public List<Order> getOrders(){
		return orderService.getAllOrders();
	}
}
