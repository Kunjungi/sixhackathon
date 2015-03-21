package com.androbook.server.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.matcher.Order;
import com.androbook.server.matcher.OrderType;
import com.androbook.server.matcher.Orderbook;

public class OrderbookInitializer {

    static private Map<Integer, String> _bookIdNameMap = new HashMap<Integer, String>();

    public IOrderbook initializeBook(int bookId, String currency) {
        Orderbook book = new Orderbook(bookId, _bookIdNameMap.get(new Integer(bookId)), currency);


        Random r = new Random();
        int refPrice = 5000+ r.nextInt(20000);
        book.setReferencePrice(refPrice);
        int no = 6+new Random().nextInt(6);
        for (int i =0; i< no; i++) {
            IOrder o  = new Order();
            int price = 5000 +r.nextInt(20000);
            o.price(price);
            o.size(1200 +r.nextInt(2000));
            o.buySellFlag(refPrice < price ? OrderType.SELL: OrderType.BUY);
            book.addOrder(o);
        }

        /*
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
        */


        return book;
    }

    public OrderbookInitializer() {
        _bookIdNameMap.put(new Integer(0), "NESTLE");
        _bookIdNameMap.put(new Integer(1), "SWISSCOM");
        _bookIdNameMap.put(new Integer(2), "HOLCIM");
        _bookIdNameMap.put(new Integer(3), "SWATCH");
        _bookIdNameMap.put(new Integer(4), "NOVARTIS");
    }

}
