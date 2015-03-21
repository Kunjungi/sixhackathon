package com.androbook.server.api;

public interface IBookListener {

	public void onOrder(IOrder order);
	public void onTrade(ITrade trade);
	public void onDelete(IOrder order);
	

}
 