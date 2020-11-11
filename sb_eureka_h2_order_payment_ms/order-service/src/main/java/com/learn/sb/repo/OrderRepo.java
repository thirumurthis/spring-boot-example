package com.learn.sb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.sb.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer>{

}
