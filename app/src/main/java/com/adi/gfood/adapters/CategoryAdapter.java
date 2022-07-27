package com.adi.gfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.activities.SearchNCategoryShowActivity;
import com.adi.gfood.models.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    static FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
            .setQuery(FirebaseDatabase.getInstance().getReference()
                    .child("categories"), snapshot -> {
                    return snapshot.getValue(Category.class);
            }).build();

    final Context context;

    public CategoryAdapter(Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {
        Picasso.get()
                .load(model.getCategoryDisplayImage())
                .placeholder(android.R.color.darker_gray)
                .fit().centerCrop()
                .into(holder.categoryImage);

        holder.categoryTextView.setText(model.getCategoryName());

        holder.itemView.setOnClickListener(v ->{
            context.startActivity(new Intent(context, SearchNCategoryShowActivity.class).putExtra("category",
                    model.getCategoryName())
            );
        });
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_view_holder, parent, false));
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        CircleImageView categoryImage;
        TextView categoryTextView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image_view_holder);
            categoryTextView = itemView.findViewById(R.id.category_name_text_view_view_holder);
        }
    }
}
