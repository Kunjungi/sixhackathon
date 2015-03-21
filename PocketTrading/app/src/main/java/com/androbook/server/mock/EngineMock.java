package com.androbook.server.mock;

import android.util.Log;

import java.util.List;
import java.util.Vector;

import com.androbook.server.api.IBookListener;
import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.api.ITradingEngine;
import com.androbook.server.matcher.Order;
import com.androbook.server.matcher.OrderType;
import com.androbook.server.matcher.Trade;

public class EngineMock implements ITradingEngine {

    private List<IBookListener> clients = new Vector<IBookListener>();

    private List<IOrderbook> books = new Vector<IOrderbook>();

    private OrderbookInitializer _initializer = new OrderbookInitializer();


    public EngineMock() {
        for (int bookId = 1; bookId < 6; bookId++) {
            books.add(_initializer.initializeBook(bookId, "CHF"));
        }
    }

    @Override
    public void sendOrder(int bookId, OrderType buySellFlag, int price, int size) {
        IOrder order = new Order();
        order.bookId(bookId).buySellFlag(buySellFlag).price(price).size(size);
        IOrderbook book = books.get(bookId);
        book.addOrder(order);

        if (order.buySellFlag() == OrderType.BUY) {
            Log.v("my", " Order BUY price : " + order.price() + " size: " + order.size());
            for (int i = book.getSellList().size() - 1; i >= 0; i--) {
                IOrder matchingOrder = book.getSellList().get(i);
                if (order.price() >= matchingOrder.price()) {
                    Log.v("my", " mmmmmm Matching price : " + matchingOrder.price() + " size: " + matchingOrder.size() + " resultingSellSize");
                    int resultingSellSize = (matchingOrder.size() > order.size()) ? order.size() : matchingOrder.size();
                    matchingOrder.size(matchingOrder.size() - resultingSellSize);
                    order.size(order.size() - resultingSellSize);
                    Log.v("my", " Result Matching price : " + matchingOrder.price() + " size: " + matchingOrder.size() + " resultingSellSize" + resultingSellSize);
                    Log.v("my", " Result Order BUY price : " + order.price() + " size: " + order.size());
                    if (matchingOrder.size() == 0) {
                        book.getSellList().remove(matchingOrder);
                        notifyDelete(order);
                    }

                    Trade trade = new Trade();
                    trade._price = order.price();
                    trade._size = resultingSellSize;
                    notifyTrade(trade);

                    if (order.size() == 0) {
                        book.getBuyList().remove(order);
                        break;
                    }

                }
            }
        } else {
            Log.v("my", " Order SELL price : " + order.price() + " size: " + order.size());
            for (int i = 0; i < book.getBuyList().size(); i++) {
                IOrder matchingOrder = book.getBuyList().get(i);
                if (order.price() <= matchingOrder.price()) {
                    Log.v("my", " mmmmmm Matching price : " + matchingOrder.price() + " size: " + matchingOrder.size() + " resultingSellSize");
                    int resultingTradeSize = matchingOrder.size() > order.size() ? order.size() : matchingOrder.size();
                    matchingOrder.size(matchingOrder.size() - resultingTradeSize);
                    order.size(order.size() - resultingTradeSize);
                    Log.v("my", " Result Matching price : " + matchingOrder.price() + " size: " + matchingOrder.size() + " resultingSellSize" + resultingTradeSize);
                    Log.v("my", " Result Order BUY price : " + order.price() + " size: " + order.size());

                    if (matchingOrder.size() == 0) {
                        book.getBuyList().remove(matchingOrder);
                        i--;
                    }

                    Trade trade = new Trade();
                    trade._price = order.price();
                    trade._size = resultingTradeSize;
                    book.setReferencePrice(order.price());
                    notifyTrade(trade);

                    if (order.size() == 0) {
                        book.getSellList().remove(order);
                        notifyDelete(order);
                        break;
                    }
                }
            }
        }


        for (IBookListener client : clients) {
            client.onOrder(order);//LoopBack
        }


    }

    private void notifyTrade(Trade trade) {
        for (IBookListener client : clients) {
            client.onTrade(trade);
        }
    }

    private void notifyDelete(IOrder order) {
        for (IBookListener client : clients) {
            client.onDelete(order);
        }
    }

    @Override
    public IOrderbook subscribe(IBookListener listener, int bookId) {
        clients.add(listener);
        return books.get(bookId);
    }

}
