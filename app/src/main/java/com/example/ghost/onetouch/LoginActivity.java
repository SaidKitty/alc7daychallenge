package com.example.ghost.onetouch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Declaring variables
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar mProgressBar;
    private Button btnSignup, btnLogin, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // A condition for when a user is already logged in.
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,
                    ProfileScreenActivity.class));
            finish();
        }

        // Set the View
        setContentView(R.layout.activity_login);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Inflating view elements in the ProfileScreenActivity
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        // Sign Up OnClick Listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,
                        SignUpActivity.class));
            }
        });

        // Password Reset OnClick Listener
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,
                        ResetPasswordActivity.class));
            }
        });

        // Login OnClick Listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter Email Address!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(),
                            "Please Enter your Password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);

                // Authenticating the User
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        mProgressBar.setVisibility(View.GONE);

                                        // If sign in fails, a message is displayed to the user.
                                        // If sign in succeeds the auth state listener will be notified and
                                        // logic to handle the signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {

                                            // An Error Occurred
                                            if (password.length() < 6) {
                                                inputPassword.setError(getString(R.string.minimum_password));
                                            } else {
                                                Toast.makeText(LoginActivity.this,
                                                        getString(R.string.auth_failed),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Intent intent = new Intent(LoginActivity.this,
                                                    ProfileScreenActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });

            }
        });
    }
}
