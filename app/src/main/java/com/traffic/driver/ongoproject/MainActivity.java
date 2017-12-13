package com.traffic.driver.ongoproject;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.*;
import android.content.*;
import android.widget.*;
import android.Manifest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.traffic.driver.ongoproject.models.Globals;

import java.lang.Object;
public class MainActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;

    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;

    private Button mLoginButton;
    private Button mSignupButton;
    private Button mForgotPassword;

    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private static final int REQUEST_LOCATION = 1;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSION_GRANTED","_PERMISSION");
            } else {
                Log.d("PERMISSION_DENIED","_PERMISSION");
            }

        }   else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestLocationService() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

//            Log.i(TAG,
//                    "Displaying camera permission rationale to provide additional context.");
//            Snackbar.make(mLayout, R.string.permission_camera_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction('Ok', new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    REQUEST_LOCATION);
//                        }
//                    })
//                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
        // END_INCLUDE(camera_permission_request)
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.

            } else {

                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }

        }



        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.signIn);
        mSignupButton = (Button) findViewById(R.id.createButton);
        mForgotPassword = (Button) findViewById(R.id.forgotPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Context _this = this;
        mLoginButton.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
               startLogin();
            }

        });
        mSignupButton.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(_this, SignupActivity.class));
            }

        });
        mForgotPassword.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
               startActivity(new Intent(_this, ResetPasswordActivity.class));
            }

        });


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user and if exists => home screen
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        if(user!=null)
        {
            Globals.currentUser = user;
            Intent toHome = new Intent(this, HomeActivity.class);
            toHome.putExtra("authUser", json);
            startActivity(toHome);
        }
    }

    public void startLogin(){
        final String email = mEmailField.getText().toString();
        final String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                mPasswordField.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            FirebaseUser user = auth.getCurrentUser();
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            if(user!=null)
                            {
                                Globals.currentUser = user;
                                Intent toHome = new Intent(MainActivity.this, HomeActivity.class);
                                toHome.putExtra("authUser", json);
                                startActivity(toHome);
                            }
                            finish();
                        }
                    }
                });

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }

    public void onStart() {
        super.onStart();

    }
}
