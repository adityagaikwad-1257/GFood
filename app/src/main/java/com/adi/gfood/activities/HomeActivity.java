package com.adi.gfood.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.adi.gfood.fragments.home.CartFragment;
import com.adi.gfood.fragments.home.HomeFragment;
import com.adi.gfood.fragments.home.ProfileFragment;
import com.adi.gfood.models.UserDetails;
import com.adi.gfood.utils.UserDetailsWatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bnv;

    HomeFragment homeFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("user_details")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserDetailsWatcher.setUserDetails(snapshot.getValue(UserDetails.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bnv = findViewById(R.id.bottom_nav_home_activity);

        bnv.setOnItemSelectedListener(this);

        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fcv_home_activity, homeFragment, "home")
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = homeFragment;

        if (item.getItemId() == R.id.cart_bn){
            fragment = cartFragment;
        }else if (item.getItemId() == R.id.profile_bn){
            fragment = profileFragment;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fcv_home_activity, fragment)
                .commit();

        return true;
    }

    @Override
    public void onBackPressed() {
        if (bnv.getSelectedItemId() != R.id.home_bn){
            bnv.setSelectedItemId(R.id.home_bn);
        }else{
            super.onBackPressed();
        }
    }
}