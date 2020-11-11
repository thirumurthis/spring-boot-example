package com.learn.sb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.sb.model.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String>{

}
