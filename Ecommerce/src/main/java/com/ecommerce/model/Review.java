package com.ecommerce.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@TypeAlias("Review")
public class Review {
	@Id
	private String reviewId;
	@DBRef
	private User userId;
	@DBRef
	private Product productId;
	private double rating;
	private String comment;
	private List<String> userImageUrls;

	public List<String> getUserImageUrls() {
		return userImageUrls;
	}
	public void setUserImageUrls(List<String> userImageUrls) {
		this.userImageUrls = userImageUrls;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public Product getProductId() {
		return productId;
	}
	public void setProductId(Product productId) {
		this.productId = productId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Review() {
		super();
        if (this.reviewId == null || this.reviewId.isEmpty()) {
            this.reviewId = UUID.randomUUID().toString();
        }
	}
	public Review(User userId, Product productId, double rating, String comment, List<String> userImageUrls) {
		this();
		this.userId = userId;
		this.productId = productId;
		this.rating = rating;
		this.comment = comment;
		this.userImageUrls = userImageUrls;
	}



}
