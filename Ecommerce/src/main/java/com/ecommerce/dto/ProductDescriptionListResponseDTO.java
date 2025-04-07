package com.ecommerce.dto;

import java.util.List;

import com.ecommerce.model.Product;

public class ProductDescriptionListResponseDTO {
    private List<Product> productList;

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public ProductDescriptionListResponseDTO(List<Product> productList) {
		super();
		this.productList = productList;
	}

	public ProductDescriptionListResponseDTO() {
		super();
	}

}
