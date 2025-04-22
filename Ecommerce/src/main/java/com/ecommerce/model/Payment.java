package com.ecommerce.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection="payment")
@TypeAlias("payment")
public class Payment {
	@Id
	private String paymentId;
	private String userId;
	private String razorPaymentLinkId;
	private String razorOrderId;
	private String razorPaymentId;
    private String paymentStatus;
	private String orderDateTime;
	public String getRazorPaymentLinkId() {
		return razorPaymentLinkId;
	}
	public void setRazorPaymentLinkId(String razorPaymentLinkId) {
		this.razorPaymentLinkId = razorPaymentLinkId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getRazorOrderId() {
		return razorOrderId;
	}
	public void setRazorOrderId(String razorOrderId) {
		this.razorOrderId = razorOrderId;
	}
	public String getRazorPaymentId() {
		return razorPaymentId;
	}
	public void setRazorPaymentId(String razorPaymentId) {
		this.razorPaymentId = razorPaymentId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Payment(String userId, String razorPaymentLinkId, String razorOrderId, String razorPaymentId, String paymentStatus, String orderDateTime) {
		this();
		this.userId = userId;
		this.razorPaymentLinkId = razorPaymentLinkId;
		this.razorOrderId = razorOrderId;
		this.razorPaymentId = razorPaymentId;
		this.paymentStatus = paymentStatus;
		this.orderDateTime = orderDateTime;
	}
	public Payment() {
        if (this.paymentId == null || this.paymentId.isEmpty()) {
            this.paymentId = "PAYMENT_" + IdGenerator.generateRandomNumber();
        }
	}
	

}
