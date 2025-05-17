package com.example.lakeside.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.lakeside.model.User;
import com.example.lakeside.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	@GetMapping("/allUsers")
	public ResponseEntity<List<User>> getUsers(){
		return new ResponseEntity<>(userService.getUsers(),HttpStatus.FOUND);
	}
	@GetMapping("userbyemail/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
			User theUser=userService.getuser(email);
			return ResponseEntity.ok(theUser);
	}
  @DeleteMapping("/deleteUser/{email}")
	public ResponseEntity<String> deleteUserByEmail(@PathVariable("email") String email ){
		userService.deleteUsers(email);
		return ResponseEntity.ok("user deleted sucessfully");
	}
	
	
	
}
