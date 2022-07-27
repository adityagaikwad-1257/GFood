package com.adi.gfood.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.gfood.R;
import com.adi.gfood.models.CartItem;
import com.adi.gfood.models.Dish;
import com.adi.gfood.models.Offer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DishShowActivity extends AppCompatActivity {

    CircleImageView backButton;

    ImageView dishNOfferImage;

    TextView offerDiscountPercentage, offerName
            , dishName, categoryName, dishContent
            , dishDescription, dishActualPrice, dishOurPrice
            , dishOfferPrice;

    Button addToCartBtn;

    public static final String SHOW_WHAT_KEY = "show_what_key";
    public static final String SHOW_OFFER = "show_offer";
    public static final String SHOW_DISH = "show_dish";
    
    Offer offer;

    Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_show_layout);

        backButton = findViewById(R.id.back_button_show);
        backButton.setOnClickListener(v -> onBackPressed());

        bindViews();

        String what = getIntent().getStringExtra(SHOW_WHAT_KEY);
        
        if (getIntent().getSerializableExtra(SHOW_OFFER) instanceof Offer){
            offer = (Offer) getIntent().getSerializableExtra(SHOW_OFFER);
        }

        if (getIntent().getSerializableExtra(SHOW_DISH) instanceof Dish){
            dish = (Dish) getIntent().getSerializableExtra(SHOW_DISH);
        }

        if (SHOW_DISH.equals(what)){
            showForDish();
        }

        if (SHOW_OFFER.equals(what) && offer != null){
            showForOffer();
        }

    }

    private void showForOffer() {
        Picasso.get()
                .load(offer.getDish().getDishImage())
                .placeholder(android.R.color.darker_gray)
                .fit()
                .centerCrop()
                .into(dishNOfferImage);

        offerDiscountPercentage.setVisibility(View.VISIBLE);
        offerDiscountPercentage.setText(offer.getDiscountPercentage() + "% OFF");

        offerName.setVisibility(View.VISIBLE);
        offerName.setText(offer.getOfferName());

        dishName.setText(offer.getDish().getDishName());

        categoryName.setVisibility(View.VISIBLE);
        categoryName.setText(offer.getDish().getDishCategory());
        dishContent.setText(offer.getDish().getDishContent());

        dishDescription.setText(offer.getDish().getDishDescription());

        dishActualPrice.setText("Rs." + offer.getDish().getActualDishPrice());
        dishOurPrice.setText("Rs." + offer.getDish().getOurDishPrice());
        dishOurPrice.setBackgroundResource(R.drawable.stroke);
        dishOfferPrice.setVisibility(View.VISIBLE);
        dishOfferPrice.setText("Rs." + offer.getDiscountedPrice());

        addToCartBtn.setOnClickListener(v -> addToCart(offer.getOfferUid(), offer.getDiscountedPrice(), offer));
    }

    private void addToCart(String uid, Integer price, Object item) {
        Log.d("aditya", "addToCart: ");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("cart");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)){
                    reference.child(uid)
                            .child("quantity")
                            .setValue(snapshot.child(uid).child("quantity").getValue(Integer.class) + 1);
                }else{
                    CartItem cartItem = new CartItem(1, price, uid);

                    if (item instanceof Offer) cartItem.setOffer((Offer) item);
                    else cartItem.setDish((Dish) item);

                    reference.child(uid)
                            .setValue(cartItem).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(DishShowActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(DishShowActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showForDish() {
        Picasso.get()
                .load(dish.getDishImage())
                .placeholder(android.R.color.darker_gray)
                .fit()
                .centerCrop()
                .into(dishNOfferImage);

        offerDiscountPercentage.setVisibility(View.GONE);
        offerName.setVisibility(View.GONE);

        dishName.setText(dish.getDishName());

        categoryName.setVisibility(View.VISIBLE);
        categoryName.setText(dish.getDishCategory());
        dishContent.setText(dish.getDishContent());

        dishDescription.setText(dish.getDishDescription());

        dishActualPrice.setText("Rs." + dish.getActualDishPrice());
        dishOurPrice.setText("Rs." + dish.getOurDishPrice());
        dishOurPrice.setBackgroundResource(R.drawable.stroke);
        dishOfferPrice.setVisibility(View.GONE);

        addToCartBtn.setOnClickListener(v -> addToCart(dish.getDishUid(), dish.getOurDishPrice(), dish));
    }

    private void bindViews() {
        dishNOfferImage = findViewById(R.id.dish_image_show);
        offerDiscountPercentage = findViewById(R.id.offer_percentage_show);
        offerName = findViewById(R.id.offer_name_show);
        dishName = findViewById(R.id.best_deal_name_show);
        categoryName = findViewById(R.id.best_deal_dish_category_show);
        dishContent = findViewById(R.id.best_deal_dish_content_show);
        dishDescription = findViewById(R.id.best_deal_dish_descriptive_show);
        dishActualPrice = findViewById(R.id.dish_original_price_best_deal_show);
        dishOurPrice = findViewById(R.id.dish_our_price_best_deal_show);
        dishOfferPrice = findViewById(R.id.dish_offer_price_best_deal_show);
        addToCartBtn = findViewById(R.id.add_to_cart_button_show);
    }
}