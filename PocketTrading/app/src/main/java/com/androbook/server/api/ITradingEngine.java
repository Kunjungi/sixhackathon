package com.androbook.server.api;

import com.androbook.server.matcher.OrderType;

public interface ITradingEngine {
	
	public void sendOrder(int bookid, OrderType buySellFlag, int price, int size);
	public IOrderbook subscribe(IBookListener listener, int bookId);

}
