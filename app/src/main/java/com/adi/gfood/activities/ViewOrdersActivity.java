package com.adi.gfood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.adi.gfood.R;
import com.adi.gfood.adapters.OrderAdapter;

public class ViewOrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        recyclerView = findViewById(R.id.fv_order_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderAdapter orderAdapter = new OrderAdapter(OrderAdapter.FULL_CART_VIEW, this);
        recyclerView.setAdapter(orderAdapter);
        orderAdapter.startListening();
    }
}