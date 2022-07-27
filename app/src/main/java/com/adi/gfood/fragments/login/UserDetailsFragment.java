package com.adi.gfood.fragments.login;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.adi.gfood.utils.UserDetailsWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsFragment extends Fragment {
    EditText firstNameEt, lastNameEt, emailEt;
    ImageView backArrow;
    Button nextBtn;

    CircleImageView userImage;

    ActivityResultLauncher<Intent> launcher;

    String displayName = "";

    String imageUri = "null";

    ProgressDialog progressDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK){
                        setupDialog();
                        progressDialog.show();
                        uploadUserImage(result.getData().getData());

                    }

                });
    }

    private void uploadUserImage(Uri data) {
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("userImages")
                .child(String.valueOf(System.currentTimeMillis()));

        reference.putFile(data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        imageUri = task.getResult().toString();
                                        userImage.setImageURI(data);
                                    }else{
                                        progressDialog.dismiss();
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("something went wrong")
                                                .setMessage(task.getException().getLocalizedMessage())
                                                .setPositiveButton("ok", ((dialog, which) -> dialog.dismiss()))
                                                .create()
                                                .show();
                                    }
                                }
                            });
                        }else{
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getContext())
                                    .setTitle("something went wrong")
                                    .setMessage(task.getException().getLocalizedMessage())
                                    .setPositiveButton("ok", ((dialog, which) -> dialog.dismiss()))
                                    .create()
                                    .show();
                        }
                    }
                });

    }

    private void setupDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("please wait");
        progressDialog.setMessage("uploading your picture");
        progressDialog.setCancelable(false);
    }

    public UserDetailsFragment() {
        super(R.layout.user_details_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEt = view.findViewById(R.id.et_first_name_user_details);
        lastNameEt = view.findViewById(R.id.et_last_name_user_details);
        emailEt = view.findViewById(R.id.et_email_user_details);
        backArrow = view.findViewById(R.id.back_arrow_user_details);
        nextBtn = view.findViewById(R.id.next_btn_user_details);
        userImage = view.findViewById(R.id.user_profile_image_user_details);

        nextBtn.setOnClickListener(v -> gotoPhoneLayout());

        backArrow.setOnClickListener(v -> getActivity().onBackPressed());

        userImage.setOnClickListener(v -> selectImage());

        Bundle bundle = getArguments();
        if (bundle != null){
            String firstName = bundle.getString("firstName");
            String lastName = bundle.getString("lastName");
            displayName = bundle.getString("displayName");
            String emailAddress = bundle.getString("email");

            firstNameEt.setText(firstName);
            lastNameEt.setText(lastName);
            emailEt.setText(emailAddress);
            emailEt.setFocusable(false);

            imageUri = bundle.getString("photoUri");

            Picasso.get()
                    .load(bundle.getString("photoUri"))
                    .placeholder(R.drawable.ic_person)
                    .fit()
                    .centerCrop()
                    .into(userImage);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(Intent.createChooser(intent, "choose option"));
    }

    private void gotoPhoneLayout() {

        if (validate()){
            PhoneNumberFragment phoneNumberFragment = new PhoneNumberFragment();

            UserDetailsWatcher.setDisplayName(firstNameEt.getText().toString().trim() + " " + lastNameEt.getText().toString().trim());
            UserDetailsWatcher.setFirstName(firstNameEt.getText().toString().trim());
            UserDetailsWatcher.setLastName(lastNameEt.getText().toString().trim());
            UserDetailsWatcher.setEmailAddress(emailEt.getText().toString().trim());
            UserDetailsWatcher.setProfileImage(imageUri);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fcv_login_page, phoneNumberFragment)
                    .addToBackStack("user_details")
                    .commit();
        }
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
