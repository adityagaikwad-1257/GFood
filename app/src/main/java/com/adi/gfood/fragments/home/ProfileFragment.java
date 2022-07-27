package com.adi.gfood.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.adi.gfood.activities.AddressActivity;
import com.adi.gfood.activities.EditProfileActivity;
import com.adi.gfood.activities.LoginActivity;
import com.adi.gfood.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "aditya";

    CircleImageView userImage;
    Toolbar toolbar;
    TextView emailTv, phoneTv;
    ProgressBar progressBar;

    LinearLayout yourAddressesBtn, editProfileBtn, signOutBtn;

    public ProfileFragment(){
        super(R.layout.profile_layout);
    }

    private void displayUserData() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("user_details")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.setVisibility(View.GONE);
                            UserDetails userDetails = snapshot.getValue(UserDetails.class);
                            Picasso.get()
                                    .load(userDetails.getUserImage())
                                    .placeholder(R.drawable.ic_person)
                                    .fit()
                                    .centerCrop()
                                    .into(userImage);

                            toolbar.setTitle("Hi, " + userDetails.getFirstName());
                            Log.d(TAG, "onDataChange: " + userDetails.getFirstName());
                            emailTv.setText(userDetails.getEmailAddress());
                            phoneTv.setText(userDetails.getPhone());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onCancelled: " + error.getMessage());
                        }
                    });
        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        userImage = view.findViewById(R.id.user_image_profile_layout);
        toolbar = view.findViewById(R.id.toolbar_profile_layout);
        emailTv = view.findViewById(R.id.email_profile_layout);
        phoneTv = view.findViewById(R.id.phone_profile_layout);
        progressBar = view.findViewById(R.id.pb_profile_layout);

        yourAddressesBtn = view.findViewById(R.id.add_address_profile_layout);
        yourAddressesBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressActivity.class)));

        editProfileBtn = view.findViewById(R.id.edit_profile_profile_layout);
        editProfileBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfileActivity.class)));

        signOutBtn = view.findViewById(R.id.sign_out_profile_layout);
        signOutBtn.setOnClickListener(v -> signOut());

        displayUserData();

        return view;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        getActivity().finish();
    }
}
