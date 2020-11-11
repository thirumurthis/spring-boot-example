package com.learn.sb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

	public String transactionId;
	public String status;
	public int orderId;
	public double amount;
}
