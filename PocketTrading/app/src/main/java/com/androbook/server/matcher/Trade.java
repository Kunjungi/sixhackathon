package com.androbook.server.matcher;

import com.androbook.server.api.ITrade;

public class Trade implements ITrade {
	
	
	public int _bookId;
	public int _price;
	public int _size;
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

}
