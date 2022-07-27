package com.adi.gfood.fragments.login;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.adi.gfood.models.UserDetails;
import com.adi.gfood.utils.AuthenticationWithPhone;
import com.adi.gfood.utils.UserDetailsWatcher;

public class OtpVerificationFragment extends Fragment {
    private static final String TAG = "aditya";
    EditText e1, e2, e3, e4, e5, e6;
    Bundle bundle;

    Button verifyButton;
    TextView resendBtn;

    ProgressDialog progressDialog;

    public OtpVerificationFragment() {
        super(R.layout.otp_verification_layout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        getContext().registerReceiver(receiver, new IntentFilter(SMS_RECEIVED_ACTION));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        getContext().unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SMS_RECEIVED_ACTION.equals(intent.getAction())){
                for (SmsMessage s: Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                    if (s.getMessageBody().contains("gfood")){
                        String otp = s.getMessageBody().substring(0, 7);
                        setOtpToEditText(otp);
                    }
                }
            }
        }
    };

    private void setOtpToEditText(String otp) {
        e1.setText(String.valueOf(otp.charAt(0)));
        e2.setText(String.valueOf(otp.charAt(1)));
        e3.setText(String.valueOf(otp.charAt(2)));
        e4.setText(String.valueOf(otp.charAt(3)));
        e5.setText(String.valueOf(otp.charAt(4)));
        e6.setText(String.valueOf(otp.charAt(5)));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        e1 = view.findViewById(R.id.otp_et_1);
        e2 = view.findViewById(R.id.otp_et_2);
        e3 = view.findViewById(R.id.otp_et_3);
        e4 = view.findViewById(R.id.otp_et_4);
        e5 = view.findViewById(R.id.otp_et_5);
        e6 = view.findViewById(R.id.otp_et_6);
        
        verifyButton = view.findViewById(R.id.verify_otp_btn);
        verifyButton.setEnabled(false);

        setupProgressBar();

        resendBtn = view.findViewById(R.id.resend_code_btn);
        resendBtn.setOnClickListener(v ->
        {
            showDialog("please wait", "while we resend you an OTP");
            AuthenticationWithPhone.resendOtp(getActivity(), UserDetailsWatcher.getPhone(), progressDialog);
        });

        UserDetails userDetails = new UserDetails(
                UserDetailsWatcher.getFirstName(),
                UserDetailsWatcher.getLastName(),
                UserDetailsWatcher.getDisplayName(),
                UserDetailsWatcher.getEmailAddress(),
                UserDetailsWatcher.getPhone(),
                UserDetailsWatcher.getProfileImage()
        );

        verifyButton.setOnClickListener(v -> {
                showDialog("please wait..", "while we authenticate the OTP");
                AuthenticationWithPhone.logInWithPhone(getContext(),
                bundle.getString("verifyCode"), getOTPFromEt(), userDetails, progressDialog
                );

                verifyButton.setEnabled(false);
                }
                );

        bundle = getArguments();

        TextView phoneTv = view.findViewById(R.id.mobile_txt_otp_verification);
        phoneTv.setText(bundle.getString("phone"));

        editTextFlow();

        return view;
    }

    private void setupProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
    }

    private void showDialog(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void editTextFlow() {

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!s.toString().trim().isEmpty())
                    e2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!s.toString().trim().isEmpty())
                    e3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    e1.requestFocus();
            }
        });

        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!s.toString().trim().isEmpty())
                    e4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    e2.requestFocus();
            }
        });

        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!s.toString().trim().isEmpty())
                    e5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    e3.requestFocus();
            }
        });

        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!s.toString().trim().isEmpty())
                    e6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    e4.requestFocus();
            }
        });

        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkOtpBoxes())
                    verifyButton.setEnabled(true);
                else
                    verifyButton.setEnabled(false);

                if (!e6.getText().toString().trim().isEmpty()){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(e1.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    e5.requestFocus();
            }
        });

    }

    private String getOTPFromEt(){
        String otp = "";

        otp += e1.getText().toString();
        otp += e2.getText().toString();
        otp += e3.getText().toString();
        otp += e4.getText().toString();
        otp += e5.getText().toString();
        otp += e6.getText().toString();

        return otp;
    }

    private boolean checkOtpBoxes(){
        if (e1.getText().toString().trim().isEmpty()){
            return false;
        }

        if (e2.getText().toString().trim().isEmpty()){
            return false;
        }

        if (e3.getText().toString().trim().isEmpty()){
            return false;
        }

        if (e4.getText().toString().trim().isEmpty()){
            return false;
        }

        if (e5.getText().toString().trim().isEmpty()){
            return false;
        }

        if (e6.getText().toString().trim().isEmpty()){
            return false;
        }

        return true;
    }
}
