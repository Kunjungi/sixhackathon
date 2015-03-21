package com.androbook.server.api;

import com.androbook.server.matcher.OrderType;

public interface IOrder {
	
	IOrder bookId(int bookId);
	IOrder size(int size);
	IOrder price(int price);
	IOrder buySellFlag(OrderType buySellFlag);
	
	int bookId();
	int size();
	int price();
	OrderType buySellFlag();

}
