package com.learn.sb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ORDER_INFO")
public class Order {

	@Id
	@GeneratedValue
	@Column(name="order_id")
    public int orderId;
	@Column(name="order_name")
	public String orderName;
	@Column(name="order_amount")
    public int orderAmount;
}
