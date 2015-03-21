package com.androbook.server.api;

import java.util.List;

public interface IOrderbook {
	
	public String getName();
	
	public List<IOrder> getBuyList();
	
	public List<IOrder> getSellList();

	public int getReferencePrice();

    public void setReferencePrice(int price);
	
	public String getCurrency();

    public void addOrder(IOrder order);

}
