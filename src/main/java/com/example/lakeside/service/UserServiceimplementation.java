package com.example.lakeside.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.lakeside.Repository.RoleRepository;
import com.example.lakeside.Repository.UserRepository;
import com.example.lakeside.exception.UserAlreadyExistsException;
import com.example.lakeside.exception.UserNotFoundException;
import com.example.lakeside.model.Role;
import com.example.lakeside.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceimplementation implements UserService{

	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	@Override
	public User registerUser(User user) {
		if( userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(user.getEmail()+" already exists");
		}
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      Role userRole=roleRepository.findByName("ROLE_USER").get();
      user.setRoles(Collections.singletonList(userRole));
      return userRepository.save(user);
	}
	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	@Transactional
	@Override
	public void deleteUsers(String email) {
		// TODO Auto-generated method stub
		User user=getuser(email);
		if(user!=null) {
			userRepository.deleteByEmail(email);
		}
		
	}
	@Override
	public User getuser(String email) {
		return userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found "+email));
	}

}
