package com.ecommerce.dto;

import java.util.List;

public class ProductGetResponseDTO {
    private String category;
    private String ProductName;
    private String productDescription;
    private double productPrice;
    private int noOfStock;
    private List<String> imageUrls;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getNoOfStock() {
		return noOfStock;
	}
	public void setNoOfStock(int noOfStock) {
		this.noOfStock = noOfStock;
	}
	public List<String> getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	public ProductGetResponseDTO(String category, String productName, String productDescription, double productPrice,
			int noOfStock, List<String> imageUrls) {
		super();
		this.category = category;
		ProductName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.noOfStock = noOfStock;
		this.imageUrls = imageUrls;
	}
	public ProductGetResponseDTO() {
		super();
	}

}
