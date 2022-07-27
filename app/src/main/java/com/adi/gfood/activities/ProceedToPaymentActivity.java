package com.adi.gfood.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.gfood.R;
import com.adi.gfood.models.CartItem;
import com.adi.gfood.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProceedToPaymentActivity extends AppCompatActivity {

    Order order;
    TextView addressTextView;
    LinearLayout contentLayout;
    TextView totalPayableTextView, errorLoadingContent;
    
    Spinner spinner;
    Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_to_payment_layout);

        order = (Order) getIntent().getSerializableExtra("order");
        addressTextView = findViewById(R.id.address_proceed_to_pay);
        contentLayout = findViewById(R.id.ll_content_proceed);

        totalPayableTextView = findViewById(R.id.total_payable_proceed);
        errorLoadingContent = findViewById(R.id.error_loading_content);

        spinner = findViewById(R.id.payment_mode_spinner);
        payBtn = findViewById(R.id.pay_btn_proceed);
        
        payBtn.setOnClickListener(v -> pay());

        setUpDetails();
    }

    private void pay(){
        order.setOrderStatus(Order.ORDERED);
        if (spinner.getSelectedItem().toString().equals("UPI")){
            Toast.makeText(ProceedToPaymentActivity.this, "Sorry :( this payment mode is not available yet", Toast.LENGTH_SHORT).show();
        }else{
            order.setPaymentMode(Order.COB);
            order.setOrderTime(getTime());
            new AlertDialog.Builder(this)
                    .setTitle("Proceed to place order")
                    .setMessage("You are placing order with payment mode CASH ON DELIVERY likewise you would pay the total payable amount to the delivery agent.\nThank you :)")
                    .setPositiveButton("place order", ((dialog, which) -> placeOrder()))
                    .setNegativeButton("cancel", ((dialog, which) -> dialog.dismiss()))
                    .show();
        }
    }

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        return DateFormat.format("dd MMM yyyy hh:mm:ss a", calendar.getTime()).toString();
    }

    private void placeOrder(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("orders");

        String key = reference.push().getKey();
        order.setOrderId(key);
        reference.child(key)
                .setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("cart")
                                .removeValue();
                        ProceedToPaymentActivity.this.finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "something went wrong... please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpDetails() {
        addressTextView.setText(order.getAddress().getAddress());
        setUpContents();
        totalPayableTextView.setText("Rs." + order.getTotalPayableAmount());
    }

    private void setUpContents() {

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("cart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()){
                            CartItem cartItem = snap.getValue(CartItem.class);
                            order.addContent(cartItem, cartItem.getUid());
                        }

                        errorLoadingContent.setVisibility(View.GONE);
                        displayContents();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void displayContents() {
        for (CartItem cartItem: order.getContents().values()){
            TextView dishName = new TextView(this);
            TextView dishPrice = new TextView(this);

            dishPrice.setText("Rs." + cartItem.getPrice() + " X " + cartItem.getQuantity());
            dishPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            dishPrice.setTextColor(getResources().getColor(R.color.black));

            if (cartItem.getDish() == null){
                dishName.setText(cartItem.getOffer().getDish().getDishName());
            }else{
                dishName.setText(cartItem.getDish().getDishName());
            }

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setWeightSum(10.0f);

            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layout.setLayoutParams(layoutParams);

            dishPrice.setLayoutParams(new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 4.0f));
            dishName.setLayoutParams(new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 6.0f));

            layout.addView(dishName);
            layout.addView(dishPrice);

            contentLayout.addView(layout);
        }
    }
}