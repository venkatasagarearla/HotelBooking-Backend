package com.example.lakeside.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String name;
 @ManyToMany(mappedBy = "roles")
 private Collection<User> users=new HashSet<>();
 public void assignRoleToUser(User user) {
user.getRoles().add(this);
this.getUsers().add(user);

 }
 
 public void removeUserFromRole(User user) {
	
	user.getRoles().remove(this);
	this.getUsers().remove(user);
	
 }
 public void removeAllUsersFromRole() {
	 if(this.getUsers()!=null) {
		 //fetches all the users
		 List<User> roleUsers=this.getUsers().stream().toList();
		 // for every roleusers  we are calling removeUserFromRole and removing the user
		 roleUsers.forEach(this:: removeUserFromRole);
	 }
 }
 
 public String getName() {
	 return name!=null?name:"";
 }
 }














