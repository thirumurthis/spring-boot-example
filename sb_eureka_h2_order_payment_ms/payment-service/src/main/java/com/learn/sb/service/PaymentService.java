package com.learn.sb.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.sb.model.Payment;
import com.learn.sb.repo.PaymentRepo;

@Service
public class PaymentService {

	@Autowired
	public PaymentRepo paymentRepo;
	
	public Payment savePayment(Payment payment) {
		
		payment.setTransactionId(UUID.randomUUID().toString());
		String status = new Random().nextBoolean()?"Success":"Failed, added to cart";
		payment.setStatus(status);
		return paymentRepo.save(payment);
	}

	public List<Payment> getPayments() {
		return paymentRepo.findAll();
	}
}
