package com.ecommerce.dto;

import java.util.List;

public class OrderAddResponseDTO {
    private List<String> successfulOrders;
    private List<String> outOfStockItems;
    private List<String> failedOrders;
	public List<String> getSuccessfulOrders() {
		return successfulOrders;
	}
	public void setSuccessfulOrders(List<String> successfulOrders) {
		this.successfulOrders = successfulOrders;
	}
	public List<String> getOutOfStockItems() {
		return outOfStockItems;
	}
	public void setOutOfStockItems(List<String> outOfStockItems) {
		this.outOfStockItems = outOfStockItems;
	}
	public List<String> getFailedOrders() {
		return failedOrders;
	}
	public void setFailedOrders(List<String> failedOrders) {
		this.failedOrders = failedOrders;
	}
	public OrderAddResponseDTO(List<String> successfulOrders, List<String> outOfStockItems, List<String> failedOrders) {
		super();
		this.successfulOrders = successfulOrders;
		this.outOfStockItems = outOfStockItems;
		this.failedOrders = failedOrders;
	}
	public OrderAddResponseDTO() {
		super();
	}

}
