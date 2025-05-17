package com.example.lakeside.service;

import com.example.lakeside.model.User;
import java .util.*;
public interface UserService {
 
	User registerUser(User user);
	List<User> getUsers();
	
	void deleteUsers(String email);
	
	User getuser(String email);
}
