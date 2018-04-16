package com.r4sh33d.medmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.r4sh33d.medmanager.R;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Log.d(TAG , "Already signed in, starting Drawer Activity");
            startActivity(new Intent(this , MainActivity.class));
            finish();
        } else {
            //not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.app_icon_med)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this , MainActivity.class));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    Log.d(TAG , "User pressed back button");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG , "No network");
                    return;
                }
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
}
