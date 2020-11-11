package com.learn.sb.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PAYMENT")
public class Payment {

	@Id
	public String transactionId;
	public String status;
	public int orderId;
	public double amount;
}
