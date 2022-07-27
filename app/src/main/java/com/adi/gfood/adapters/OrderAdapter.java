package com.adi.gfood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.models.CartItem;
import com.adi.gfood.models.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderAdapter extends FirebaseRecyclerAdapter<Order, OrderAdapter.OrderViewHolder> {
    static FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
            .setQuery(FirebaseDatabase.getInstance().getReference()
            .child("users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .child("orders").limitToFirst(5).orderByChild("orderTime"), snapshot -> snapshot.getValue(Order.class))
            .build();

    public static final int CART_VIEW = 1;
    public static final int FULL_CART_VIEW = 0;

    int what;
    Context context;

    public OrderAdapter(int what, Context context){
        super(options);
        this.what = what;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (what == CART_VIEW){
            return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.order_view_holder,
                    parent, false
            ), what);
        }else {
            return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.full_view_order_view_holder,
                    parent, false
            ), what);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
        for (CartItem cartItem: model.getContents().values()){
            String image = "";
            if (cartItem.getDish() == null){
                image = cartItem.getOffer().getDish().getDishImage();
            }else {
                image = cartItem.getDish().getDishImage();
            }

            Picasso.get()
                    .load(image)
                    .placeholder(android.R.color.darker_gray)
                    .fit().centerCrop()
                    .into(holder.dishImage);
            break;
        }

        holder.status.setText(model.getOrderStatus().toUpperCase());
        holder.price.setText("Rs." + model.getTotalPayableAmount());
        holder.time.setText(model.getOrderTime());

        if (what == FULL_CART_VIEW){
            holder.address.setText(model.getAddress().getAddress());
            if (model.getOrderStatus().equals(Order.DELIVERED))
                holder.deliveryTime.setText(model.getDeliveryTime());

            holder.uid.setText(model.getOrderId());

            holder.contentLayout.removeAllViews();

            for (CartItem cartItem: model.getContents().values()){
                TextView dishName = new TextView(context);
                TextView dishPrice = new TextView(context);

                dishPrice.setText("Rs." + cartItem.getPrice() + " X " + cartItem.getQuantity());
                dishPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                dishPrice.setTextColor(context.getResources().getColor(R.color.black));

                if (cartItem.getDish() == null){
                    dishName.setText(cartItem.getOffer().getDish().getDishName());
                }else{
                    dishName.setText(cartItem.getDish().getDishName());
                }

                LinearLayout layout = new LinearLayout(context);
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

                holder.contentLayout.addView(layout);
            }
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        ImageView dishImage;
        TextView status, price, time;

        TextView address, deliveryTime, uid;
        LinearLayout contentLayout;
        public OrderViewHolder(@NonNull View itemView, int what) {
            super(itemView);
            if (what == OrderAdapter.CART_VIEW) {
                dishImage = itemView.findViewById(R.id.dish_image_order_vh);
                status = itemView.findViewById(R.id.status_order_vh);
                price = itemView.findViewById(R.id.total_payable_order_vh);
                time = itemView.findViewById(R.id.time_order_vh);
            }else {
                dishImage = itemView.findViewById(R.id.dish_image_fv_order);
                status = itemView.findViewById(R.id.order_status_fv_order);
                price = itemView.findViewById(R.id.total_payable_fv_order);
                time = itemView.findViewById(R.id.order_time_fv_order);
                address = itemView.findViewById(R.id.shipping_address_fv_order_vh);
                deliveryTime = itemView.findViewById(R.id.delivery_time_fv_order);
                uid = itemView.findViewById(R.id.order_id_fv_order);
                contentLayout = itemView.findViewById(R.id.ll_content_fv_order);
            }

        }
    }
}
