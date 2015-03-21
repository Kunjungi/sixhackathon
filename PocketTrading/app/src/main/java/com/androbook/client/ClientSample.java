package com.androbook.client;

import com.androbook.server.api.EngineFactory;
import com.androbook.server.api.IBookListener;
import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.api.ITrade;
import com.androbook.server.api.ITradingEngine;
import com.androbook.server.matcher.OrderType;

public class ClientSample implements IBookListener{

	private boolean mockEngine = true;
	private ITradingEngine engine = EngineFactory.getEngine(mockEngine);
	
	private IOrderbook _currentBook;
	
	@Override
	public void onOrder(IOrder order) {
		System.out.println("Received add Order command ! " + order);
		//Update view		
	}

	@Override
	public void onTrade(ITrade trade) {
		System.out.println("Received Trade ! " + trade);

		//Update view		
	}

	@Override
	public void onDelete(IOrder order) {
		System.out.println("Received add Order command ! " + order);
		////Update view	
	}
	
	public ClientSample() {
	}
	
	public void sendOrder(int bookid, OrderType buySellFlag, int price, int size) {
		engine.sendOrder(bookid, buySellFlag, price, size);
	}
	
	/**
	 * subscribe and get order book content as a snapshot
	 * */
	public IOrderbook getSnapshot(int bookId) {
		_currentBook = engine.subscribe(this, bookId);
		return _currentBook;
	}

	public static void main(String[] args) {
		int initialBookId = 1;
		ClientSample client = new ClientSample();
		
		IOrderbook book = client.getSnapshot(initialBookId);
		String name = book.getName();
	
		try {
			int buyPrice = 10000;//centimes
			int sellPrice = 11000;//centimes
			for (int i=0; i<4; i++) {
				client.sendOrder(1, OrderType.BUY,buyPrice, 200);
				client.sendOrder(1, OrderType.SELL,sellPrice, 200);		
				Thread.sleep(2000);		
				buyPrice = buyPrice -100;//-1CHF
				sellPrice = sellPrice +100;//1CHF
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	}
	

	

}
