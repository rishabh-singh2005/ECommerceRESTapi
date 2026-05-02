package com.example.ecom.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.dtos.OrderItemDto;
import com.example.ecom.dtos.OrderResponseDto;
import com.example.ecom.entities.OrderEntity;
import com.example.ecom.entities.OrderItemEntity;
import com.example.ecom.entities.ProductEntity;
import com.example.ecom.entities.UserEntity;
import com.example.ecom.exceptions.OrderNotFoundException;
import com.example.ecom.exceptions.ProductNotFoundException;
import com.example.ecom.repositories.OrderRepository;
import com.example.ecom.repositories.ProductRepository;

@Service
public class AdminService {

	@Autowired
    private final OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public static final String UPLOAD_DIR = "uploads/product_img/";

    AdminService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
	
	public String addProduct(String name, String discription, double price, MultipartFile file) throws IOException {
		
		File directory = new File(UPLOAD_DIR);
		
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
		
		String path = UPLOAD_DIR+fileName;
		
		File destination = new File(path);
		
		file.transferTo(destination);
		
		ProductEntity product = new ProductEntity();
		product.setName(name);
		product.setDescription(discription);
		product.setPrice(price);
		product.setImagePath(fileName); //Store only file name
		
		productRepository.save(product);
		
		
		
		return "Product "+product.getName()+" Uploaded Successfully";
		
		
	}
	
	public String updateProductPrice(Long id, double price) {
		
		
		Optional<ProductEntity> ops_product = productRepository.findById(id);
		
		if(ops_product.isPresent()) {
		
		ProductEntity product =  ops_product.get();
		
		product.setPrice(price);
		
		productRepository.save(product);
		
		return "Price of product "+product.getName()+" updated successfully to "+product.getPrice();
		}
		else {
			throw new ProductNotFoundException("Product not found with id "+id);
		}
		
		
	}
	
	public String deleteProduct(Long id) {
		
		
		Optional<ProductEntity> ops_product = productRepository.findById(id);
		
		ProductEntity product;
		
		if(ops_product.isPresent()) {
			product = ops_product.get();
		}
		else {
			throw new ProductNotFoundException("Product not found");
		}
			
		
		productRepository.delete(product);
		
		
		return "Product "+product.getName()+ " deleted successfully";
		
		
	}
	
	public OrderResponseDto getCustomerOrders(int id) {
		
		OrderEntity order = orderRepository.findById(id);
		
		if(order==null) {
			throw new OrderNotFoundException("Order not found");
		}
		
		OrderResponseDto respOrder = new OrderResponseDto();
		
		respOrder.setOrderId(order.getId());
		respOrder.setCustomerName(order.getUser().getName());
		respOrder.setStatus(order.getOrderStatus().name());
		respOrder.setAmount(order.getOrderAmount());
		
		List<OrderItemDto> listItems = new ArrayList<>();
		
		for(OrderItemEntity item : order.getItems()) {
			
			OrderItemDto itemDto = new OrderItemDto();
			
			itemDto.setProductId(item.getId());
			itemDto.setPrice(item.getPrice());
			itemDto.setQuantity(item.getQuantity());
			
			listItems.add(itemDto);		
		}
		
		respOrder.setItems(listItems);
		
		return respOrder;
		
		
	}

}
