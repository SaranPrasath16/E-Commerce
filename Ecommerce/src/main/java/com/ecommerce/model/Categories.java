package com.ecommerce.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="categories")
@TypeAlias("Categories")
public class Categories {
	@Id
	private String categoryId;
	private String categoryName;
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Categories(String categoryName) {
		this();
		this.categoryName = categoryName;
	}
	public Categories() {
		super();
        if (this.categoryId == null || this.categoryId.isEmpty()) {
            this.categoryId = UUID.randomUUID().toString();
        }
	}

}

