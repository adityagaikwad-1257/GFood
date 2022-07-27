package com.adi.gfood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.models.CartItem;
import com.adi.gfood.models.Dish;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CartAdapter extends FirebaseRecyclerAdapter<CartItem, CartAdapter.CartViewHolder> {
    static FirebaseRecyclerOptions<CartItem> options = new FirebaseRecyclerOptions.Builder<CartItem>()
            .setQuery(FirebaseDatabase.getInstance().getReference()
            .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .child("cart"), snapshot -> snapshot.getValue(CartItem.class)).build();

    public CartAdapter(){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartItem model) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dish dish;
                if (snapshot.child("dishes").hasChild(model.getUid())){
                    dish = snapshot.child("dishes")
                            .child(model.getUid())
                            .getValue(Dish.class);
                    holder.offerPrice.setVisibility(View.GONE);
                }else {
                    dish = snapshot.child("offers")
                            .child(model.getUid())
                            .child("dish")
                            .getValue(Dish.class);

                    holder.offerPrice.setText("Rs." + model.getPrice());
                    holder.offerPrice.setVisibility(View.VISIBLE);
                    holder.ourPrice.setBackgroundResource(R.drawable.stroke);
                }

                Picasso.get()
                        .load(dish.getDishImage())
                        .placeholder(android.R.color.darker_gray)
                        .fit().centerCrop().into(holder.dishImage);

                holder.dishName.setText(dish.getDishName());
                holder.dishContent.setText(dish.getDishContent());
                holder.actualPrice.setText("Rs." + dish.getActualDishPrice());
                holder.ourPrice.setText("Rs." + dish.getOurDishPrice());
                holder.displayQuantity.setText(""+model.getQuantity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.incrementQuantity.setOnClickListener(v -> {
            if (model.getQuantity() < 10){
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("cart")
                        .child(model.getUid())
                        .child("quantity")
                        .setValue(model.getQuantity() + 1);
            }
        });

        holder.decrementQuantity.setOnClickListener(v -> {
            if (model.getQuantity() > 1){
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("cart")
                        .child(model.getUid())
                        .child("quantity")
                        .setValue(model.getQuantity() - 1);
            }else if (model.getQuantity() == 1){
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("cart")
                        .child(model.getUid())
                        .removeValue();
            }
        });

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cart_item_view_holder,
                parent, false
        ));
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView dishImage;
        TextView dishName, dishContent, actualPrice, ourPrice, offerPrice;
        TextView displayQuantity;
        Button incrementQuantity, decrementQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dish_image_cart_item_vh);
            dishName = itemView.findViewById(R.id.dish_name_cart_item_vh);
            dishContent = itemView.findViewById(R.id.dish_content_cart_item_vh);
            actualPrice = itemView.findViewById(R.id.dish_original_price_cart_view_holder);
            ourPrice = itemView.findViewById(R.id.dish_our_price_cartl_view_holder);
            offerPrice = itemView.findViewById(R.id.dish_offer_price_cart_vh);
            displayQuantity = itemView.findViewById(R.id.display);
            incrementQuantity = itemView.findViewById(R.id.increment);
            decrementQuantity = itemView.findViewById(R.id.decrement);
        }
    }

}
