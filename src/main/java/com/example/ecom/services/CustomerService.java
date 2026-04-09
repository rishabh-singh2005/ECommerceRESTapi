package com.example.ecom.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecom.dtos.OrderItemDto;
import com.example.ecom.dtos.OrderRequestDto;
import com.example.ecom.entities.OrderEntity;
import com.example.ecom.entities.ProductEntity;
import com.example.ecom.entities.UserEntity;
import com.example.ecom.entities.OrderEntity.OrderStatus;
import com.example.ecom.entities.OrderItemEntity;
import com.example.ecom.repositories.OrderRepository;
import com.example.ecom.repositories.ProductRepository;
import com.example.ecom.repositories.UserRepository;

@Service
public class CustomerService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	public Page<ProductEntity> getAllProducts(int page, int size, String sortBy){
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy) );
		
		Page<ProductEntity> productPage = productRepository.findAll(pageable);
		
		return productPage;
		
	}
	
	public Page<ProductEntity> searchProduct(String name, int page, int size){
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<ProductEntity> page_product = productRepository.findByNameContainingIgnoreCase(name, pageable);
		
		return  page_product;
		
	}
	
	public String createOrder(OrderRequestDto request, String email) {
		
		Optional<UserEntity> ops_user = userRepository.findByEmail(email);
		
		UserEntity user = ops_user.get();
		
		OrderEntity order = new OrderEntity();
		
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.PENDING);
		
		List<OrderItemEntity> orderItems = new ArrayList<>();
		
		double totalAmount = 0;
		
		for(OrderItemDto dto : request.getItems()) {
			
			Optional<ProductEntity> ops_product = productRepository.findById(dto.getProductId());
			
			ProductEntity product = ops_product.get();
			
			OrderItemEntity item = new OrderItemEntity();
			
			item.setProduct(product);
			item.setQuantity(dto.getQuantity());
			item.setPrice(product.getPrice());
			item.setOrder(order);
			
			totalAmount = product.getPrice()*dto.getQuantity();
			
			orderItems.add(item);
		}
		
		order.setItems(orderItems);
		order.setOrderAmount(totalAmount);
		
		orderRepository.save(order);
		
		return "Order created successfully with amount "+totalAmount;
		
		
		
		
	}

}
