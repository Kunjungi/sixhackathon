package com.androbook.server.matcher;

import com.androbook.server.api.IOrder;

public class Order implements IOrder{

	int _bookId;
	int _price;
	int _size;
	OrderType _buySellFlag;
	
	@Override
	public IOrder bookId(int bookId) {
		_bookId = bookId;
		return this;
	}

	@Override
	public IOrder size(int size) {
		_size = size;
		return this;
	}

	@Override
	public IOrder price(int price) {
		_price = price;
		return this;
	}

	@Override
	public int bookId() {
		return _bookId;
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public int price() {
		return _price;
	}

	@Override
	public IOrder buySellFlag(OrderType buySellFlag) {
		_buySellFlag = buySellFlag;
		return this;
	}

	@Override
	public OrderType buySellFlag() {
		return _buySellFlag;
	}

}
