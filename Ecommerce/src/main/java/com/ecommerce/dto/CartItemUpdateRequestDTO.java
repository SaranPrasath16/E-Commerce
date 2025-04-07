package com.ecommerce.dto;

import com.ecommerce.model.Product;

public class CartItemUpdateRequestDTO {

    private Product productId;
    private double price;
    private int quantity;
    private boolean selectedForPayment;
	public boolean isSelectedForPayment() {
		return selectedForPayment;
	}
	public void setSelectedForPayment(boolean selectedForPayment) {
		this.selectedForPayment = selectedForPayment;
	}
	public Product getProductId() {
		return productId;
	}
	public void setProductId(Product productId) {
		this.productId = productId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public CartItemUpdateRequestDTO(Product productId, double price, int quantity, boolean selectedForPayment) {
		super();
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
		this.selectedForPayment = selectedForPayment;
	}
	public CartItemUpdateRequestDTO() {
		super();
	}

}
