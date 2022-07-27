package com.adi.gfood.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adi.gfood.activities.HomeActivity;
import com.adi.gfood.models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class AuthenticationWithPhone {
    private static PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private AuthenticationWithPhone(){}

    public static PhoneAuthCredential credential(String verificationId, String otp){
        return PhoneAuthProvider.getCredential(verificationId, otp);
    }

    public static void logInWithPhone(Context context, String verificationId, String otp, UserDetails userDetails, ProgressDialog progressDialog){
        logInWithCredentials(context, credential(verificationId, otp), userDetails, progressDialog);
    }

    public static void logInWithCredentials(Context context, PhoneAuthCredential phoneAuthCredential, UserDetails userDetails, ProgressDialog progressDialog){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            saveUserData(context, userDetails);
                        }else{
                            new AlertDialog.Builder(context)
                                    .setTitle("Oops..!!")
                                    .setMessage("you have entered a wrong otp")
                                    .setPositiveButton("ok", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .create()
                                    .show();
                        }
                    }
                });
    }

    static void saveUserData(Context context, UserDetails userDetails){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDetails.setUid(uid);

        reference.child("users")
                .child(uid)
                .child("user_details")
                .setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    context.startActivity(new Intent(context, HomeActivity.class));
                    ((Activity) context).finish();
                }else{
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void resendOtp(Activity activity, String phone, ProgressDialog progressDialog){
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phone)
                .setActivity(activity)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks(activity, progressDialog))
                .setForceResendingToken(forceResendingToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }

    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks(Context context, ProgressDialog progressDialog){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                new AlertDialog.Builder(context)
                        .setTitle("Oops!!")
                        .setMessage("something went wrong while resending you the otp please try again")
                        .setPositiveButton("ok", ((dialog, which) -> dialog.dismiss()))
                        .create()
                        .show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(context, "code sent", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };
    }

    public static void setForceResendingToken(PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        AuthenticationWithPhone.forceResendingToken = forceResendingToken;
    }
}
