package com.example.ecom.dtos;

import java.util.List;

public class OrderRequestDto {
	
	private List<OrderItemDto> items;

	public List<OrderItemDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}

}
