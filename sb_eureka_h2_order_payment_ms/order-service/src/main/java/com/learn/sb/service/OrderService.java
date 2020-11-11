package com.learn.sb.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learn.sb.model.Order;
import com.learn.sb.model.OrderStatus;
import com.learn.sb.model.Payment;
import com.learn.sb.repo.OrderRepo;

@Service
public class OrderService {

	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	RestTemplate template;
	
	public final String URL= "http://PAYMENT-SERVICE/payment/pay";
	
	public Order getOrders(int orderId) {
		return orderRepo.findById(orderId).orElse(new Order());
	}
	
	public OrderStatus addOrder(Order order) {
		OrderStatus orderStatus = new OrderStatus();
		Order persistedOrder = orderRepo.save(order);
		Payment payment = paymentprocess(persistedOrder);
		orderStatus.setOrder(persistedOrder);
		//String status = new Random().nextBoolean()?"Success":"Failed, added to cart"
		orderStatus.setPayment(payment);
		orderStatus.setStatus(payment.getStatus());
        return orderStatus;
	}
	
	public Payment paymentprocess(Order order) {
		Payment payment = new Payment();
		payment.setOrderId(order.getOrderId());
		payment.setAmount(order.getOrderAmount());
		HttpEntity<Payment> request = new HttpEntity<>(payment);
		return template.postForObject(URL, request, Payment.class);
	}

	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}
}
