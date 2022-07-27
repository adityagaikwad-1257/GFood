package com.adi.gfood.adapters;

import static com.adi.gfood.activities.DishShowActivity.SHOW_DISH;
import static com.adi.gfood.activities.DishShowActivity.SHOW_WHAT_KEY;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.activities.DishShowActivity;
import com.adi.gfood.models.Dish;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class BestDealAdapter extends FirebaseRecyclerAdapter<Dish, BestDealAdapter.BestDealViewHolder> {

    static Query query = FirebaseDatabase.getInstance().getReference()
            .child("dishes")
            .orderByChild("bestDeal")
            .equalTo(1);

    static FirebaseRecyclerOptions<Dish> options = new FirebaseRecyclerOptions.Builder<Dish>()
            .setQuery(query, snapshot -> snapshot.getValue(Dish.class))
            .build();

    final Context context;
    LoadListener loadListener;

    public BestDealAdapter(Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public BestDealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BestDealViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.best_deal_view_holder,
                parent,
                false
        ));
    }

    @Override
    protected void onBindViewHolder(@NonNull BestDealViewHolder holder, int position, @NonNull Dish dish) {
        loadListener.onLoad();
        Picasso.get()
                .load(dish.getDishImage())
                .placeholder(android.R.color.darker_gray)
                .fit().centerCrop().into(holder.dishImage);

        holder.dishName.setText(dish.getDishName());
        holder.dishDescription.setText(dish.getDishContent());
        holder.originalPrice.setText("Rs." + dish.getActualDishPrice());
        holder.ourPrice.setText("Rs." + dish.getOurDishPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishShowActivity.class);
            intent.putExtra(SHOW_WHAT_KEY, SHOW_DISH);
            intent.putExtra(SHOW_DISH, dish);
            context.startActivity(intent);
        });
    }

    public void setLoadListener(LoadListener loadListener){
        this.loadListener = loadListener;
    }

    public interface LoadListener {
        void onLoad();
    }

    public static class BestDealViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName, dishDescription, originalPrice, ourPrice;

        public BestDealViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.best_deal_dish_image_view_holder);
            dishName = itemView.findViewById(R.id.best_deal_name_view_holder);
            dishDescription = itemView.findViewById(R.id.best_deal_dish_descriptive_view_holder);
            originalPrice = itemView.findViewById(R.id.dish_original_price_best_deal_view_holder);
            ourPrice = itemView.findViewById(R.id.dish_our_price_best_deal_view_holder);
        }
    }
}
