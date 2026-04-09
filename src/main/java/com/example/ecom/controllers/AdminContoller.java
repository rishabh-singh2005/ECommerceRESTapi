package com.example.ecom.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.response.ApiResponse;
import com.example.ecom.services.AdminService;

@RestController
@PreAuthorize("hasRole('ADMIN')") //Only admin allowed
@RequestMapping("/admin")
public class AdminContoller {
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/add-product")
	public ResponseEntity<ApiResponse<String>> addProduct(@RequestParam String name, @RequestParam String description, @RequestParam double price, @RequestParam MultipartFile file) throws IOException{
		
		String msg = adminService.addProduct(name, description, price, file);
		
		ApiResponse<String> response = new ApiResponse<>("Product added successfully", msg);
		
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/update-product-price/{id}")
	public ResponseEntity<ApiResponse<String>> updateProductPrice(@PathVariable Long id, @RequestParam double price){
		
		String msg = adminService.updateProductPrice(id, price);
		
		ApiResponse<String> response = new ApiResponse<>("Product price updated successfully", msg);
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@DeleteMapping("/delete-product/{id}")
	public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
		
		String msg = adminService.deleteProduct(id);
		
		ApiResponse<String> response = new ApiResponse<>("Product deleted Successfully", msg);
		
		return ResponseEntity.ok(response);
		
		
		
	}

}
