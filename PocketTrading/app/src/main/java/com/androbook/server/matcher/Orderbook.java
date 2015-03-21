package com.androbook.server.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.api.ITrade;

public class Orderbook implements IOrderbook {

    private String _name;

    private int _referencePrice = 9837;

    private ArrayList<IOrder> _buyOrders = new ArrayList<IOrder>();

    private ArrayList<IOrder> _sellOrders = new ArrayList<IOrder>();

    private String _currency;

    private int _bookId;

    private ITrade lastTrade;

    public Orderbook(int bookId, String name, String currency) {
        _bookId = bookId;
        _name = name;
        _currency = currency;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public List<IOrder> getBuyList() {
        return _buyOrders;
    }

    @Override
    public List<IOrder> getSellList() {
        return _sellOrders;
    }

    @Override
    public int getReferencePrice() {
        return _referencePrice;
    }

    @Override
    public void setReferencePrice(int price) {
        this._referencePrice = price;
    }

    @Override
    public String getCurrency() {
        return _currency;
    }


    public void addOrder(IOrder order) {
        if (order.buySellFlag().equals(OrderType.BUY)) {
            _buyOrders.add(order);
            Collections.sort(_buyOrders, OrderComparator);
        } else {
            _sellOrders.add(order);
            Collections.sort(_sellOrders, OrderComparator);
        }
    }


    public static Comparator<IOrder> OrderComparator = new Comparator<IOrder>() {
        public int compare(IOrder o1, IOrder o2) {
            Integer p1 = o1.price();
            Integer p2 = o2.price();
            return p2.compareTo(p1);
        }};
}
