package com.example.healthprolatestversion;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button loginbtn, regbtn;
    TextInputLayout usernamelogin, passwordlogin;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView regnowtxt, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        regbtn = findViewById(R.id.regbtn);
        loginbtn = findViewById(R.id.loginbtn);
        usernamelogin = findViewById(R.id.usernamelogin);
        passwordlogin = findViewById(R.id.passwordlogin);
        regnowtxt = findViewById(R.id.regnowtxt);
        forgotPassword = findViewById(R.id.forgotPassword);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        regnowtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent = new Intent(Login.this, Register.class);
                startActivity(regintent);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpass = new Intent(Login.this, Resetpass.class);
                startActivity(forgotpass);
                finish();
            }
        });

    }

    private Boolean validateUsername(String val) {

        if (val.isEmpty()) {
            usernamelogin.setError("Field cannot be empty");
            return false;
        } else {
            usernamelogin.setError(null);
            usernamelogin.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(String val) {

        if (val.isEmpty()) {
            passwordlogin.setError("Field cannot be empty");
            return false;
        } else {
            passwordlogin.setError(null);
            passwordlogin.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser() {
        String email = usernamelogin.getEditText().getText().toString();
        String password = passwordlogin.getEditText().getText().toString();

        if (!validateUsername(email)) {
            Toast.makeText(Login.this, "The email is not yet registered", Toast.LENGTH_SHORT).show();
        } else if (!validatePassword(password)) {
            Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        } else {
            signin(email, password);
        }
    }



    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user data
                            mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        saveusertosharedpreference(String.valueOf(task.getResult().getValue()));
                                    }
                                }
                            });

                            Intent intent = new Intent(Login.this, SplashScreen.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException() != null && task.getException().getMessage() != null) {
                                String errorMessage = task.getException().getMessage();
                                if (errorMessage.contains("no user record")) {
                                    Toast.makeText(Login.this, "The email is not yet registered", Toast.LENGTH_SHORT).show();
                                } else if (errorMessage.contains("password is invalid")) {
                                    Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }





    public void saveusertosharedpreference(String data) {

             Context context = getApplicationContext();
             SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
             SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PREF_DATA", data);
                editor.apply();

        Log.d("sharedPreference", data);
    }
    @Override
    public void onBackPressed() {

    }
}