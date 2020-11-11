package com.learn.sb.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.sb.model.Payment;
import com.learn.sb.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	public PaymentService paymentService;
	
	@PostMapping("/pay")
	public Payment payProcess(@RequestBody Payment payment) {
		return paymentService.savePayment(payment);
	}
	
	@GetMapping("/payments")
	public List<Payment> getPayments(){
		return paymentService.getPayments();
	}
}
