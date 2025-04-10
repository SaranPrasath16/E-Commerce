package com.ecommerce.dto;

public class ReviewUpdateRequestDTO {
    private String reviewId;
    private double rating;
    private String comment;
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
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
	public ReviewUpdateRequestDTO(String reviewId, double rating, String comment) {
		super();
		this.reviewId = reviewId;
		this.rating = rating;
		this.comment = comment;
	}
	public ReviewUpdateRequestDTO() {
		super();
	}

}
