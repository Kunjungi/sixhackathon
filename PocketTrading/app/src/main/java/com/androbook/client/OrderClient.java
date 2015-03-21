package com.androbook.client;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.androbook.client.ui.OrderActivity;
import com.androbook.server.api.IBookListener;
import com.androbook.server.api.IOrder;
import com.androbook.server.api.ITrade;

/**
 * Created by q on 20/03/15.
 */
public class OrderClient implements IBookListener {

    final OrderActivity activity;
    Handler myHandler;

    public OrderClient(OrderActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onTrade(ITrade trade) {
        System.out.println("Received Trade ! " + trade);
        activity.renderOrderPage(null);
    }

    @Override
    public void onOrder(IOrder order) {
        System.out.println("Received add Order command ! " + order);
        activity.renderOrderPage(order);
        waitAndRender();

    }

    @Override
    public void onDelete(IOrder order) {
        System.out.println("Received add Order command ! " + order);
        activity.renderOrderPage(null);
    }

    private int retry = 1;

    public void waitAndRender() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.v("my", "run called");
                activity.renderOrderPage(null);
            }
        }, 2000);
    }



}


