package com.example.ecom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecom.entities.OrderEntity;
import java.util.List;
import java.util.Optional;

import com.example.ecom.entities.UserEntity;


public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	
	List<OrderEntity> findByUser(UserEntity user);
	
	OrderEntity findById(int id);
}
