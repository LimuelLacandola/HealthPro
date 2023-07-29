package com.example.healthprolatestversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Resetpass extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Button forgotPasswordBtn, cancelforgotpass;
    TextInputLayout usernamelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        usernamelogin = findViewById(R.id.usernamelogin);
        cancelforgotpass = findViewById(R.id.cancelforgotpass);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });

        cancelforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resetpass.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void resetPassword() {
        String email = usernamelogin.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            usernamelogin.setError("Field cannot be empty");
            return;
        } else {
            usernamelogin.setError(null);
            usernamelogin.setErrorEnabled(false);
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Resetpass.this, "Recovery link sent to your email.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Resetpass.this, "Failed to send recovery link, account not registered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Resetpass.this, Login.class);
        startActivity(intent);
        finish();
    }
}
