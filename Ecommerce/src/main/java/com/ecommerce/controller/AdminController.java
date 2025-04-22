package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.User;
import com.ecommerce.services.admin.AdminImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/superadmin")
public class AdminController {
	
	private final AdminImpl adminImpl;

	public AdminController(AdminImpl adminImpl) {
		super();
		this.adminImpl = adminImpl;
	}
	
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.getAllUsers());
    }
    
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam("user_Id") String userId, HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.deleteUser(userId));
    }

}
