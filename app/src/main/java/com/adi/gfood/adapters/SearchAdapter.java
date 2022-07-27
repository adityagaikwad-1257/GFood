package com.adi.gfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.activities.DishShowActivity;
import com.adi.gfood.models.Dish;
import com.squareup.picasso.Picasso;

public class SearchAdapter extends ListAdapter<Dish, SearchAdapter.SearchViewHolder> {
    static DiffUtil.ItemCallback<Dish> callback = new DiffUtil.ItemCallback<Dish>() {
        @Override
        public boolean areItemsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getDishUid().equals(newItem.getDishUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getDishName().equals(newItem.getDishName()) &&
                    oldItem.getDishContent().equals(newItem.getDishContent()) &&
                    oldItem.getOurDishPrice().equals(newItem.getOurDishPrice());
        }
    };

    final Context context;

    public SearchAdapter(Context context) {
        super(callback);
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.search_item_view_holder,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Dish dish = getItem(position);
        Picasso.get()
                .load(dish.getDishImage())
                .placeholder(android.R.color.darker_gray)
                .fit().centerCrop()
                .into(holder.dishImage);

        holder.dishName.setText(dish.getDishName());
        holder.dishContent.setText(dish.getDishContent());
        holder.dishPrice.setText("Rs." + dish.getOurDishPrice());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishShowActivity.class);
            intent.putExtra(DishShowActivity.SHOW_WHAT_KEY, DishShowActivity.SHOW_DISH);
            intent.putExtra(DishShowActivity.SHOW_DISH, dish);
            context.startActivity(intent);
        });
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView dishImage;
        TextView dishName, dishContent, dishPrice;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            dishContent = itemView.findViewById(R.id.dish_content_search);
            dishName = itemView.findViewById(R.id.dish_name_search);
            dishImage = itemView.findViewById(R.id.dish_image_search_item);
            dishPrice = itemView.findViewById(R.id.dish_price_search);
        }
    }
}
