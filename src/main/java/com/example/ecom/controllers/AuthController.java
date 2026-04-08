package com.example.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecom.dtos.LoginReqDto;
import com.example.ecom.dtos.LoginResponseDto;
import com.example.ecom.dtos.RegisterRequestDto;
import com.example.ecom.response.ApiResponse;
import com.example.ecom.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register-customer")
	public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody RegisterRequestDto reg_user){
		
		String msg = authService.registerCustomer(reg_user);
		
		ApiResponse<String> response = new ApiResponse("Customer registered successfully", msg);
		
		return  ResponseEntity.ok(response);
	}
	
	@PostMapping("/register-admin")
	public ResponseEntity<ApiResponse<String>> registerAdmin(@Valid @RequestBody RegisterRequestDto reg_admin){
		
		String msg = authService.registerAdmin(reg_admin);
		
		ApiResponse<String> response = new ApiResponse("Admin registered successfully", msg);
		
		return ResponseEntity.ok(response);		
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> loginUser(@RequestBody LoginReqDto log_req){
		
		LoginResponseDto log_res = authService.loginUser(log_req);
		
		ApiResponse<LoginResponseDto> response = new ApiResponse("User login successfully" , log_res);
		
		return ResponseEntity.ok(response);
		
	}

}
