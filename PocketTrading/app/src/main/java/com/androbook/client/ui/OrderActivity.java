
package com.androbook.client.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androbook.client.ClientSample;
import com.androbook.client.OrderClient;
import com.androbook.server.api.EngineFactory;
import com.androbook.server.api.IBookListener;
import com.androbook.server.api.IOrder;
import com.androbook.server.api.IOrderbook;
import com.androbook.server.api.ITrade;
import com.androbook.server.api.ITradingEngine;
import com.androbook.server.matcher.OrderType;
import com.androbook.server.matcher.Orderbook;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OrderActivity extends ActionBarActivity {

    private boolean mockEngine = true;
    private ITradingEngine engine = EngineFactory.getEngine(mockEngine);

    private IOrderbook _currentBook;
    private Button buyButton;
    private Button sellButton;
    private EditText editPrice;
    private EditText editAmount;
    private int bookId = 0;
    private OrderClient client;
    private IOrderbook book;
    private TableRow.LayoutParams lpCell;

    public void setBuyButtonText(String text) {
        buyButton.setText(text);
    }

    public OrderActivity() {
    }

    public void sendOrder(int bookid, OrderType buySellFlag, int price, int size) {
        engine.sendOrder(bookid, buySellFlag, price, size);
    }

    /**
     * subscribe and get order book content as a snapshot
     */
    public IOrderbook getSnapshot(OrderClient theClient, int bookId) {
        _currentBook = engine.subscribe(theClient, bookId);
        return _currentBook;
    }


    private String formatPrice(int price, IOrderbook book) {
        Float p = 0.0f + price / 100;
        return String.format("%.2f", p);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new OrderClient(this);

        book = getSnapshot(client, bookId);

        renderOrderPage(null);
    }

    public void renderOrderPage(IOrder lastOrder) {

        TableLayout ll = new TableLayout(this);
        TableLayout.LayoutParams lpTable = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        ll.setPadding(2, 2, 2, 2);
        ll.setStretchAllColumns(true);
        ll.setLayoutParams(lpTable);
        ll.setBackgroundColor(Color.BLACK);
        ActionBar.LayoutParams lpPage = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams lpScroll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        lpCell = new TableRow.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lpCell.setMargins(2, 0, 0, 0);

        RelativeLayout header = addPageHeader();
        addTableHeader(ll);


        addOrderGrid(ll, lastOrder);
        RelativeLayout bottom = addOrderForm();


        //Log.v("my", "id header: " + header.getId());
        lpScroll.addRule(RelativeLayout.BELOW, header.getId());
        lpScroll.addRule(RelativeLayout.ABOVE, bottom.getId());

        // Render page layout

        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(lpScroll);
        sv.setBackgroundColor(Color.WHITE);
        sv.addView(ll);
        RelativeLayout linear = new RelativeLayout(this);

        linear.addView(header);
        linear.addView(bottom);
        linear.addView(sv);

        setContentView(linear);


        ll.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (bookId < engine.noBooks) {
                    bookId++;
                } else {
                    bookId = 0;
                }

                book = getSnapshot(client, bookId);
                renderOrderPage(null);
            }

            public void onSwipeLeft() {
                if (bookId >= 1) {
                    bookId--;
                } else {
                    bookId = engine.noBooks-1;
                }
                book = getSnapshot(client, bookId);
                renderOrderPage(null);
            }

            public void onSwipeTop() {
            }

            public void onSwipeBottom() {
            }

        });
    }

    private RelativeLayout addOrderForm() {
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1);
        TableLayout.LayoutParams lpNarrow = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1);

        RelativeLayout.LayoutParams lpBottom = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //LinearLayout.LayoutParams lpOrderRow = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Order Row
        RelativeLayout bottom = new RelativeLayout(this);
        bottom.setId(99);
        bottom.setLayoutParams(lpBottom);
        LinearLayout orderRow = new LinearLayout(this);

        LinearLayout.LayoutParams orderRowParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        orderRow.setLayoutParams(orderRowParam);
        orderRow.setOrientation(LinearLayout.HORIZONTAL);
        orderRow.setWeightSum(4);
        //orderRow.setLayoutParams(lpOrderRow);
        editAmount = new EditText(this);
        editAmount.setLayoutParams(lp);
        editAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        editAmount.setHint("Amount");
        orderRow.addView(editAmount);

        editPrice = new EditText(this);
        editPrice.setLayoutParams(lp);
        editPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        editPrice.setHint("Price");
        orderRow.addView(editPrice);

        buyButton = new Button(this);
        buyButton.setBackgroundColor(Color.RED);
        buyButton.setTextColor(Color.WHITE);
        buyButton.setLayoutParams(lpNarrow);
        buyButton.setText("Buy");
        orderRow.addView(buyButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sellBuyListener(OrderType.BUY);
            }
        });


        sellButton = new Button(this);
        sellButton.setBackgroundColor(Color.BLUE);
        sellButton.setTextColor(Color.WHITE);
        sellButton.setLayoutParams(lpNarrow);
        sellButton.setText("Sell");
        orderRow.addView(sellButton);

        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sellBuyListener(OrderType.SELL);
            }
        });
        bottom.addView(orderRow);

        editAmount.requestFocus();
        return bottom;
    }

    private void addOrderGrid(TableLayout ll, IOrder lastOrder) {
        List<IOrder> buyList = book.getBuyList();
        List<IOrder> sellList = book.getSellList();

        List<IOrder> allList = new ArrayList<>();
        allList.addAll(sellList);
        allList.addAll(buyList);
        Collections.sort(allList, Orderbook.OrderComparator);

        for (int i = 0; i < allList.size(); i++) {
            IOrder o = allList.get(i);

            if (o.buySellFlag() == OrderType.SELL) {
                TableRow tbrow = new TableRow(this);

                tbrow.setBackgroundColor(Color.BLACK);

                tbrow.setPadding(2, 2, 2, 2);
                TextView tv1 = new TextView(this);
                tv1.setTextColor(Color.BLUE);
                tv1.setBackgroundColor(Color.WHITE);
                if (lastOrder != null && lastOrder.price() == o.price()) {
                    tv1.setBackgroundColor(Color.YELLOW);
                }
                tv1.setId(o.bookId());
                tv1.setText("" + o.size());
                tv1.setPadding(2, 0, 2, 0);
                tv1.setLayoutParams(lpCell);
                tbrow.addView(tv1);
                TextView tv2 = new TextView(this);
                tv2.setId(10000 + o.bookId());
                tv2.setTextColor(Color.BLUE);
                tv2.setBackgroundColor(Color.WHITE);
                if (lastOrder != null && lastOrder.price() == o.price()) {
                    tv2.setBackgroundColor(Color.YELLOW);
                }
                tv2.setPadding(2, 0, 2, 0);
                tv2.setLayoutParams(lpCell);
                tv2.setText("" + formatPrice(o.price(), book));
                tbrow.addView(tv2);
                tbrow.addView(emptyRow());
                tbrow.addView(emptyRow());
                ll.addView(tbrow);
            } else {
                //Log.v("my", "buy:" + i + o.size());
                TableRow tbrow = new TableRow(this);

                tbrow.setBackgroundColor(Color.BLACK);
                tbrow.setPadding(2, 2, 2, 2);
                tbrow.addView(emptyRow());
                tbrow.addView(emptyRow());
                TextView tv2 = new TextView(this);
                tv2.setId(10000 + o.bookId());
                tv2.setBackgroundColor(Color.WHITE);
                if (lastOrder != null && lastOrder.price() == o.price()) {
                    tv2.setBackgroundColor(Color.YELLOW);
                }
                tv2.setTextColor(Color.RED);
                tv2.setPadding(2, 0, 2, 0);
                tv2.setLayoutParams(lpCell);
                tv2.setText("" + formatPrice(o.price(), book));
                tbrow.addView(tv2);
                TextView tv1 = new TextView(this);
                tv1.setId(o.bookId());
                tv1.setBackgroundColor(Color.WHITE);
                if (lastOrder != null && lastOrder.price() == o.price()) {
                    tv1.setBackgroundColor(Color.YELLOW);
                }
                tv1.setTextColor(Color.RED);
                tv1.setLayoutParams(lpCell);
                tv1.setPadding(2, 0, 2, 0);
                tv1.setText("" + o.size());
                tbrow.addView(tv1);
                ll.addView(tbrow);
            }
        }

    }

    private RelativeLayout addPageHeader() {
        RelativeLayout.LayoutParams lpTop = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTop.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout header = new RelativeLayout(this);
        header.setId(3);
        header.setLayoutParams(lpTop);
        TextView companyText = new TextView(this);
        companyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        companyText.setTextColor(Color.BLACK);
        companyText.setPadding(10, 6, 3, 6);
        companyText.setText(book.getName() + "   " + formatPrice(book.getReferencePrice(), book) + " " + book.getCurrency());
        header.addView(companyText);
        return header;
    }

    private void addTableHeader(TableLayout ll) {
        // Table Header
        TableRow theader = new TableRow(this);
        ll.addView(theader);
        theader.setPadding(2, 2, 2, 2);
        addHeaderCell(true, "Bid Size", theader);
        addHeaderCell(true, "Bid", theader);
        addHeaderCell(false, "Ask", theader);
        addHeaderCell(false, "Ask Size", theader);
    }

    private void addHeaderCell(boolean bid, String label, TableRow theader) {
        TextView tv1 = new TextView(this);
        tv1.setTextColor(Color.WHITE);
        tv1.setBackgroundColor(bid ? Color.BLUE : Color.RED);
        tv1.setLayoutParams(lpCell);
        tv1.setPadding(2, 0, 2, 0);
        tv1.setText(label);
        theader.addView(tv1);
    }


    private TextView emptyRow() {
        TextView empty = new TextView(this);
        empty.setBackgroundColor(Color.WHITE);
        empty.setPadding(2, 0, 2, 0);
        empty.setLayoutParams(lpCell);

        return empty;
    }

    private void sellBuyListener(OrderType orderType) {

        try {
            Float prix = (Float.valueOf(editPrice.getText().toString())) * 100;
            if (prix > 0) {
                sendOrder(bookId, orderType, prix.intValue(), Integer.valueOf(editAmount.getText().toString()));
                editAmount.setText("");
                editPrice.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}

