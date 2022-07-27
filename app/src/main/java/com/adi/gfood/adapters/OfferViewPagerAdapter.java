package com.adi.gfood.adapters;

import static com.adi.gfood.activities.DishShowActivity.SHOW_OFFER;
import static com.adi.gfood.activities.DishShowActivity.SHOW_WHAT_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.gfood.R;
import com.adi.gfood.activities.DishShowActivity;
import com.adi.gfood.models.Offer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OfferViewPagerAdapter extends FirebaseRecyclerAdapter<Offer, OfferViewPagerAdapter.OfferViewHolder> {

     static FirebaseRecyclerOptions<Offer> options = new FirebaseRecyclerOptions.Builder<Offer>()
            .setQuery(FirebaseDatabase.getInstance().getReference()
            .child("offers"), snapshot ->
                snapshot.getValue(Offer.class)
            ).build();

     final Context context;

    public OfferViewPagerAdapter(Context context){
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.offfer_view_pager_view_holder,
                parent,
                false
        ));
    }

    @Override
    protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull Offer offer) {
        Picasso.get()
                .load(offer.getDish().getDishImage())
                .placeholder(android.R.color.darker_gray)
                .centerCrop()
                .fit()
                .into(holder.offerImage);

        holder.offerImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, DishShowActivity.class);
            intent.putExtra(SHOW_OFFER, offer);
            intent.putExtra(SHOW_WHAT_KEY, SHOW_OFFER);
            context.startActivity(intent);
        });
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{
        ImageView offerImage;
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerImage = itemView.findViewById(R.id.offer_image_view_pager_view_holder);
        }
    }
}
