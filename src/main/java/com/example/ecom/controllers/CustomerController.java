package com.example.ecom.controllers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecom.dtos.OrderRequestDto;
import com.example.ecom.entities.OrderEntity;
import com.example.ecom.entities.ProductEntity;
import com.example.ecom.response.ApiResponse;
import com.example.ecom.services.AdminService;
import com.example.ecom.services.CustomerService;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')") //Both allowed
@RequestMapping("/app")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/product-image/{fileName}")
	public ResponseEntity<Resource> getProductImage(@PathVariable String fileName) throws IOException {
		
		String UPLOAD_DIR = adminService.UPLOAD_DIR;

	    Path path = Paths.get(UPLOAD_DIR + fileName); //This builds the complete path of the image file.
	    //Paths.get() converts it into a Java Path object.

	    Resource resource = new UrlResource(path.toUri()); //UrlResource wraps the file so Spring can stream it to the client.
	    //Example : file:///D:/RESTapiUploadFile/product_img/171234_laptop.jpg
	    
	    if(!resource.exists()){
	        throw new RuntimeException("Image not found");
	    }

	    return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path)) //It automatically detects file type
	            .header(HttpHeaders.CONTENT_DISPOSITION, //.header() simply adds: Header Name : Header Value
	                    "inline; filename=\"" + resource.getFilename() + "\"") //inline : Show file inside browser/Postman
	            .body(resource);
	    
	    //Example : 
//	    HTTP/1.1 200 OK
//	    Content-Disposition: inline; filename="laptop.jpg"
//	    If you use "attachment; instead of "inline; browser will force download it
	}

	@GetMapping("/get-products")
	public ResponseEntity<ApiResponse<Page<ProductEntity>>> getAllProducts(@RequestParam(defaultValue = "0") int page ,@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy) {
		
		Page<ProductEntity> res_page = customerService.getAllProducts(page, size, sortBy);
		
		List<ProductEntity> lis_product = res_page.getContent();
		
		String baseUrl = "https://ecommercerestapi-production.up.railway.app";
		
		for(ProductEntity p : lis_product) {
			p.setImagePath(baseUrl + "/app/product-image/" + p.getImagePath());
		}
		
		ApiResponse<Page<ProductEntity>> response = new ApiResponse<>("List of all products", res_page);
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/search-product")
	public ResponseEntity<ApiResponse<Page<ProductEntity>>> searchProduct(@RequestParam String name, @RequestParam(defaultValue = "0") int page ,@RequestParam(defaultValue = "5") int size){
		
		Page<ProductEntity> products = customerService.searchProduct(name, page, size);
		
		for(ProductEntity p : products) {
			p.setImagePath("http://localhost:8080/app/product-image/" + p.getImagePath());
		}
		
		ApiResponse<Page<ProductEntity>> response = new ApiResponse<Page<ProductEntity>>("List of products", products);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/order-product")
	public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody OrderRequestDto request, Authentication auth){
		
		String email = auth.getName();
		
		String msg = customerService.createOrder(request, email);
		
		ApiResponse<String> response = new ApiResponse<>("Order successfull", msg);
		
		return ResponseEntity.ok(response);
		
		
	}
	
	@GetMapping("/my-orders")
	public ResponseEntity<ApiResponse<List<OrderEntity>>> getMyOrders(Authentication auth){
		
		String email = auth.getName();
		
		List<OrderEntity> lis_orders = customerService.getMyOrders(email);
		
		ApiResponse<List<OrderEntity>> response = new ApiResponse<>("Order Details ", lis_orders);
		
		return ResponseEntity.ok(response);
	}
	
}
