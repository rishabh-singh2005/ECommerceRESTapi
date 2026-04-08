package com.example.ecom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecom.dtos.LoginReqDto;
import com.example.ecom.dtos.LoginResponseDto;
import com.example.ecom.dtos.RegisterRequestDto;
import com.example.ecom.entities.UserEntity;
import com.example.ecom.entities.UserEntity.Role;
import com.example.ecom.repositories.UserRepository;
import com.example.ecom.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepositoty;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public UserEntity convertToUserEntity(RegisterRequestDto regReq){
		
		UserEntity user = new UserEntity();
		
		
		user.setName(regReq.getName());
		user.setEmail(regReq.getEmail());
		user.setPassword(passwordEncoder.encode(regReq.getPassword()));
		user.setRole(Role.CUSTOMER);
		
		return user;
		
	}

	@Override
	public String registerCustomer(RegisterRequestDto regReq) {
		
		UserEntity user  = convertToUserEntity(regReq);
		
		UserEntity regUser = userRepositoty.save(user);
		
		return "User "+regUser.getName()+" registerd successfully";
	}

	@Override
	public String registerAdmin(RegisterRequestDto regAdmin) {
		UserEntity admin = convertToUserEntity(regAdmin);
		admin.setRole(Role.ADMIN);
		UserEntity user = userRepositoty.save(admin);
		return "Admin "+user.getName()+" registerd successfully";
	}

	@Override
	public LoginResponseDto loginUser(LoginReqDto log_req) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(log_req.getEmail(),log_req.getPassword()));
		
		String token = jwtUtil.generateToken(log_req.getEmail());
		return new LoginResponseDto(token);
	}

}
