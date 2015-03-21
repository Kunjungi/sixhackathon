package com.androbook.server.matcher;

public enum OrderType {

	BUY ("Buy"),
	SELL ("Sell");
	
	private String label;
	
	 OrderType(String lab) {
		label = lab;
	}
	
}
