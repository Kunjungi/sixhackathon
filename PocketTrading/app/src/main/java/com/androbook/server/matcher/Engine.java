package com.androbook.server.matcher;

import java.util.List;
import java.util.Vector;

import com.androbook.server.api.IBookListener;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.api.ITradingEngine;

public class Engine implements ITradingEngine {
	
	private List<IBookListener> clients = new Vector<IBookListener>();
	
	private List<Orderbook> books = new Vector<Orderbook>();
	
	public Engine() {
		
	}

	@Override
	public void sendOrder(int bookId, OrderType buySellFlag, int price, int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IOrderbook subscribe(IBookListener listener, int bookId) {
		clients.add(listener);
		return books.get(bookId);
	}

}
