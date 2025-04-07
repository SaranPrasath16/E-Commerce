package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="orders")
@TypeAlias("Orders")
public class Orders {
	@Id
	private String orderId;
	@DBRef
	private User userId;
	private List<CartItems> cartItems;
	private double totalAmount;
	private int noOfItems;
	private LocalDateTime orderDateTime;
	private String status;
	
	public int getNoOfItems() {
		return noOfItems;
	}
	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}
	public List<CartItems> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItems> cartItems) {
		this.cartItems = cartItems;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Orders() {
        if (this.orderId == null || this.orderId.isEmpty()) {
            this.orderId = UUID.randomUUID().toString();
        }
	}
	public Orders(User userId, List<CartItems> cartItems, double totalAmount,int noOfItems,
			LocalDateTime orderDateTime, String status) {
		this();
		this.userId = userId;
		this.cartItems = cartItems;
		this.totalAmount = totalAmount;
		this.noOfItems=noOfItems;
		this.orderDateTime = orderDateTime;
		this.status = status;
	}

	
	
	

}
