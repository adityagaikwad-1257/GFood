package com.adi.gfood.fragments.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.adi.gfood.models.UserDetails;
import com.adi.gfood.utils.AuthenticationWithPhone;
import com.adi.gfood.utils.UserDetailsWatcher;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberFragment extends Fragment {

    Button sendOtpBtn;
    EditText phoneEt;

    ProgressDialog progressDialog;

    public PhoneNumberFragment() {
        super(R.layout.phone_number_layout);
    }

    ActivityResultLauncher<String> requestLauncher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requestLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result)
                        sendOtp();
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        sendOtpBtn = view.findViewById(R.id.send_otp_btn);
        phoneEt = view.findViewById(R.id.phone_number_et_phone_number_layout);

        progressDialog = new ProgressDialog(getContext());

        sendOtpBtn.setOnClickListener(v -> sendOtp());

        return view;
    }

    private void sendOtp() {

        if (phoneEt.getText().toString().trim().matches("")){
            phoneEt.setError("phone required");
        }else if (phoneEt.getText().toString().length() <10){
            phoneEt.setError("invalid phone number");
        }else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestLauncher.launch(Manifest.permission.RECEIVE_SMS);
        }
        else{
//            for testing purposes
            showDialog();
            UserDetailsWatcher.setPhone("+91"+phoneEt.getText().toString());
            FirebaseAuthSettings settings = FirebaseAuth.getInstance().getFirebaseAuthSettings();
            settings.setAutoRetrievedSmsCodeForPhoneNumber("+911234567890", "123456");

            PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber("+91" + phoneEt.getText().toString())
                    .setActivity(getActivity())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .build();

            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            UserDetails userDetails = new UserDetails(
                    UserDetailsWatcher.getFirstName(),
                    UserDetailsWatcher.getLastName(),
                    UserDetailsWatcher.getDisplayName(),
                    UserDetailsWatcher.getEmailAddress(),
                    UserDetailsWatcher.getPhone(),
                    UserDetailsWatcher.getProfileImage()
            );

            AuthenticationWithPhone.logInWithCredentials(getContext(), phoneAuthCredential, userDetails, progressDialog);
//            if device has the number inserted
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), "error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d("aditya", "onVerificationFailed: "+e.getLocalizedMessage());
            progressDialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(getContext(), "code sent", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            Bundle bundle = new Bundle();
            bundle.putString("verifyCode", s);
            bundle.putString("phone", "+91" + phoneEt.getText().toString());

            OtpVerificationFragment otpVerificationFragment = new OtpVerificationFragment();
            otpVerificationFragment.setArguments(bundle);

            AuthenticationWithPhone.setForceResendingToken(forceResendingToken);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fcv_login_page, otpVerificationFragment)
                    .addToBackStack("otp_verification")
                    .commit();

//            if the device doesn't have the number inserted
        }
    };

    private void showDialog(){
        progressDialog.dismiss();
        progressDialog.setCancelable(false);
        progressDialog.setTitle("please wait..");
        progressDialog.setMessage("we are verifying your Phone Number");
        progressDialog.create();
        progressDialog.show();
    }
}
