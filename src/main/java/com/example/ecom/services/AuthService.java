package com.example.ecom.services;

import org.springframework.stereotype.Service;

import com.example.ecom.dtos.LoginReqDto;
import com.example.ecom.dtos.LoginResponseDto;
import com.example.ecom.dtos.RegisterRequestDto;

@Service
public interface AuthService {
	
	public String registerCustomer(RegisterRequestDto regReq);
	public String registerAdmin(RegisterRequestDto regAdmin);
	public LoginResponseDto loginUser(LoginReqDto log_req);

}
