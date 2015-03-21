package com.androbook.server.mock;

import java.util.HashMap;
import java.util.Map;

import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.matcher.Order;
import com.androbook.server.matcher.OrderType;
import com.androbook.server.matcher.Orderbook;

public class OrderbookInitializer {

    static private Map<Integer, String> _bookIdNameMap = new HashMap<Integer, String>();

    public IOrderbook initializeBook(int bookId, String currency) {
        Orderbook book = new Orderbook(bookId, _bookIdNameMap.get(new Integer(bookId)), currency);

        int price = 10000;//centimes
        int size = 250;
        IOrder order1 = new Order();
        //BUY
        book.addOrder(order1.buySellFlag(OrderType.BUY).size(size).price(price));
        IOrder order2 = new Order();
        order2.price(order1.price() - 100).size(350).buySellFlag(OrderType.BUY);
        book.addOrder(order2);
        IOrder order3 = new Order();
        order3.price(order2.price() - 200).size(150).buySellFlag(OrderType.BUY);
        book.addOrder(order3);

        //SELL
        IOrder order4 = new Order();
        price = 12000;
        order4.buySellFlag(OrderType.SELL).size(300).price(price);
        book.addOrder(order4);
        IOrder order5 = new Order();
        order5.size(100).price(order4.price() + 100).buySellFlag(OrderType.SELL);
        book.addOrder(order5);
        IOrder order6 = new Order();
        order6.size(200).price(order5.price() + 200).buySellFlag(OrderType.SELL);
        book.addOrder(order6);
        return book;
    }

    public OrderbookInitializer() {
        _bookIdNameMap.put(new Integer(1), "NESTLE");
        _bookIdNameMap.put(new Integer(2), "SWISSCOM");
        _bookIdNameMap.put(new Integer(3), "HOLCIM");
        _bookIdNameMap.put(new Integer(4), "SWATCH");
        _bookIdNameMap.put(new Integer(5), "NOVARTIS");
    }

}
