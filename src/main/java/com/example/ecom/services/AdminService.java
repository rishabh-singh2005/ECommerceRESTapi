package com.example.ecom.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecom.entities.ProductEntity;
import com.example.ecom.repositories.ProductRepository;

@Service
public class AdminService {
	
	@Autowired
	private ProductRepository productRepository;
	
	private static final String UPLOAD_DIR = "D:\\RESTapiUploadFile\\product_img\\";
	
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
		product.setImagePath(path);
		
		productRepository.save(product);
		
		
		
		return "Product "+product.getName()+" Uploaded Successfully";
		
		
	}
	
	public String updateProductPrice(Long id, double price) {
		
		Optional<ProductEntity> ops_product = productRepository.findById(id);
		
		ProductEntity product =  ops_product.get();
		
		product.setPrice(price);
		
		productRepository.save(product);
		
		return "Price of product "+product.getName()+" updated successfully to "+product.getPrice();
		
		
	}

}
