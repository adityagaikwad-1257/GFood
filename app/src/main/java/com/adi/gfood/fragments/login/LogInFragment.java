package com.adi.gfood.fragments.login;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class LogInFragment extends Fragment {

    GoogleSignInClient googleSignInClient;
    ProgressBar progressBar;

    Button phoneBtn;

    private static final String TAG = "aditya";

    ActivityResultLauncher<Intent> launcher;
    
    public LogInFragment(){
        super(R.layout.login_layout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            GoogleSignInAccount googleSignInAccount = task.getResult();

                            Bundle bundle = new Bundle();
                            bundle.putString("firstName", googleSignInAccount.getGivenName());
                            bundle.putString("lastName", googleSignInAccount.getFamilyName());
                            bundle.putString("displayName", googleSignInAccount.getDisplayName());
                            bundle.putString("email", googleSignInAccount.getEmail());
                            bundle.putString("photoUri", googleSignInAccount.getPhotoUrl().toString());

                            UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
                            userDetailsFragment.setArguments(bundle);

                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fcv_login_page, userDetailsFragment)
                                    .addToBackStack("user_details")
                                    .commit();

                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onAttach: " + task.getException().getLocalizedMessage());
                            googleSignInClient.signOut();
                        }

                    }else {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onAttach: 2 " + result);
                        Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        googleSignInClient.signOut();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        SignInButton gsiBtn = view.findViewById(R.id.gsi_login);
        progressBar = view.findViewById(R.id.pb_login);

        phoneBtn = view.findViewById(R.id.phone_login_btn);
        phoneBtn.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fcv_login_page, new UserDetailsFragment())
                    .addToBackStack("user_details")
                    .commit();
        });

        gsiBtn.setOnClickListener(v ->{ signInWithGoogle();});

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        googleSignInClient.signOut();

        return view;
    }

    private void signInWithGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        launcher.launch(googleSignInClient.getSignInIntent());
    }
}




