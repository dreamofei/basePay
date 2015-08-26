package com.eland.basepay.component.model;

public class OrderInfo {

	private String orderInfo;

	public OrderInfo(String content) {
		orderInfo=content;
	}
	
	public String GetContent(){
		return orderInfo;
	}
}
