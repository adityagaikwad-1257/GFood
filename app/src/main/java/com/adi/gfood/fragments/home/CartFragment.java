package com.adi.gfood.fragments.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.adi.gfood.R;
import com.adi.gfood.activities.AddressActivity;
import com.adi.gfood.activities.ProceedToPaymentActivity;
import com.adi.gfood.activities.ViewOrdersActivity;
import com.adi.gfood.adapters.CartAdapter;
import com.adi.gfood.adapters.OrderAdapter;
import com.adi.gfood.models.Address;
import com.adi.gfood.models.CartItem;
import com.adi.gfood.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CartFragment extends Fragment {

    public CartFragment(){
        super(R.layout.cart_layout);
    }

    RecyclerView cartRecyclerView;
    ViewPager2 orderRecyclerView;
    TextView noCartItems;
    Button payBtn;

    TextView viewAllOrders;

    Integer total = 0;

    ActivityResultLauncher<Intent> launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        cartRecyclerView = view.findViewById(R.id.cart_orders_recyclerview);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CartAdapter cartAdapter = new CartAdapter();
        cartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.startListening();

        orderRecyclerView = view.findViewById(R.id.your_orders_view_pager);
        orderRecyclerView.setVisibility(View.VISIBLE);
        OrderAdapter orderAdapter = new OrderAdapter(OrderAdapter.CART_VIEW, getContext());
        orderRecyclerView.setAdapter(orderAdapter);
        orderAdapter.startListening();

        noCartItems = view.findViewById(R.id.no_cart_items_txt);
        noCartItems.setVisibility(View.GONE);
        payBtn = view.findViewById(R.id.pay_btn_cart);
        payBtn.setVisibility(View.GONE);

        registerLauncher();

        viewAllOrders = view.findViewById(R.id.view_all_orders_btn);
        viewAllOrdersListener();

        viewAllOrders.setOnClickListener(v ->
                startActivity(new Intent(getContext(), ViewOrdersActivity.class)));

        payBtn.setOnClickListener(v -> {
            pay();
        });

        setTotalAndText();

        return view;
    }

    private void viewAllOrdersListener() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) viewAllOrders.setVisibility(View.VISIBLE);
                        else viewAllOrders.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void registerLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Address address = (Address) result.getData().getSerializableExtra("address");
                        proceedToPay(address);
                    }
                });
    }

    private void proceedToPay(Address address) {
        Order order = new Order();
        order.setAddress(address);
        order.setTotalPayableAmount(total);
        Intent intent = new Intent(getContext(), ProceedToPaymentActivity.class);
        intent.putExtra("order", order);
        getContext().startActivity(intent);
    }

    private void pay() {
        Intent intent = new Intent(getContext(), AddressActivity.class);
        intent.putExtra("selection", "selection");
        launcher.launch(intent);
    }

    private void setTotalAndText() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = 0;
                if (snapshot.getChildrenCount() == 0){
                    noCartItems.setVisibility(View.VISIBLE);
                    payBtn.setVisibility(View.GONE);
                }else {
                    noCartItems.setVisibility(View.GONE);
                    for (DataSnapshot snap: snapshot.getChildren()){
                        CartItem cartItem = snap.getValue(CartItem.class);
                        total += (cartItem.getQuantity() * cartItem.getPrice());
                    }

                    payBtn.setText("Pay Rs. " + total);
                    payBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
