package com.adi.gfood.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.gfood.R;
import com.adi.gfood.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView userImage;
    EditText firstNameEt, lastNameEt, emailEt;
    Button updateBtn;

    ImageView backArrow;

    DatabaseReference reference;

    ActivityResultLauncher<Intent> launcher;

    ProgressDialog progressDialog;

    String imageUri = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_layout);

        firstNameEt = findViewById(R.id.et_first_name_user_details);
        lastNameEt = findViewById(R.id.et_last_name_user_details);
        emailEt = findViewById(R.id.et_email_user_details);
        userImage = findViewById(R.id.user_profile_image_user_details);
        updateBtn = findViewById(R.id.next_btn_user_details);
        backArrow = findViewById(R.id.back_arrow_user_details);

        backArrow.setOnClickListener(v -> onBackPressed());

        updateBtn.setOnClickListener(v -> {
            if (validate()){
                showProgressDialog("please wait", "while we update your profile details");
                updateData();
            }
        });

        userImage.setOnClickListener(v -> selectImage());

        updateBtn.setText("Update details");

        reference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("user_details");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayData();
        }

        registerLauncher();
        setupProgressDialog();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void registerLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK){
                        showProgressDialog("Please wait..", "while we upload your profile picture");
                        uploadUserImage(result.getData().getData());
                    }

                });
    }

    private void showProgressDialog(String s, String s1) {
        progressDialog.setTitle(s);
        progressDialog.setMessage(s1);
        progressDialog.show();
    }

    private void uploadUserImage(Uri data) {
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("userImages")
                .child(String.valueOf(System.currentTimeMillis()));

        reference.putFile(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        reference.getDownloadUrl().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                progressDialog.dismiss();
                                imageUri = task1.getResult().toString();
                                userImage.setImageURI(data);
                            }else{
                                progressDialog.dismiss();
                                new AlertDialog.Builder(EditProfileActivity.this)
                                        .setTitle("something went wrong")
                                        .setMessage(task1.getException().getLocalizedMessage())
                                        .setPositiveButton("ok", ((dialog, which) -> dialog.dismiss()))
                                        .create()
                                        .show();
                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        new AlertDialog.Builder(EditProfileActivity.this)
                                .setTitle("something went wrong")
                                .setMessage(task.getException().getLocalizedMessage())
                                .setPositiveButton("ok", ((dialog, which) -> dialog.dismiss()))
                                .create()
                                .show();
                    }
                });

    }

    private void selectImage() {
        launcher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"));
    }

    private void updateData() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstNameEt.getText().toString());
        userDetails.put("lastName", lastNameEt.getText().toString());
        userDetails.put("displayName", firstNameEt.getText().toString() + " " + lastNameEt.getText().toString());
        userDetails.put("emailAddress", emailEt.getText().toString());
        userDetails.put("userImage", imageUri);

        Log.d("aditya", "updateData: " + userDetails);

        reference.updateChildren(userDetails)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        Toast.makeText(EditProfileActivity.this, "Profile changes updated.", Toast.LENGTH_SHORT).show();
                        EditProfileActivity.this.finish();
                    }else{
                        Toast.makeText(EditProfileActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);
                firstNameEt.setText(userDetails.getFirstName());
                lastNameEt.setText(userDetails.getLastName());
                emailEt.setText(userDetails.getEmailAddress());

                imageUri = userDetails.getUserImage();

                Picasso.get()
                        .load(userDetails.getUserImage())
                        .placeholder(R.drawable.ic_person)
                        .fit().centerCrop()
                        .into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validate(){
        boolean allOk = true;
        if (firstNameEt.getText().toString().trim().matches("")){
            allOk = false;
            firstNameEt.setError("Requires");
        }

        if (emailEt.getText().toString().trim().matches("")){
            allOk = false;
            emailEt.setError("requires");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString().trim()).matches()){
            allOk = false;
            emailEt.setError("email invalid");
        }

        return allOk;
    }
}
