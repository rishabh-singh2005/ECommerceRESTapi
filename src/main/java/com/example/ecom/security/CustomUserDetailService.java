package com.example.ecom.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.ecom.entities.UserEntity;
import com.example.ecom.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) {
		
		Optional<UserEntity> ops_user =  userRepository.findByEmail(email);
		
		if(!ops_user.isPresent()) {
			throw new UsernameNotFoundException("User not found");
		}
		
		UserEntity user = ops_user.get();
		
		return User.builder()
				.username(user.getEmail())
				.authorities("ROLE_"+user.getRole())
				.password(user.getPassword())
				.build();
		
		
		
		
		
		
	}

}
