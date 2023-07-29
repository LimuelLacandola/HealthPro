package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Heartrate extends AppCompatActivity {
    private ImageView heartback;
    private RatingBar ratingBar;
    private EditText editText;
    private Button submitBtn;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        heartback = findViewById(R.id.heartback);
        ratingBar = findViewById(R.id.ratingBar);
        editText = findViewById(R.id.editText);
        submitBtn = findViewById(R.id.submitBtn);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        heartback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Heartrate.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDataToDatabase();
                editText.setText("");
            }
        });
    }

    private void saveDataToDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            float rating = ratingBar.getRating();
            String feedback = editText.getText().toString().trim();

            // Save the rating and feedback in the database
            DatabaseReference userRef = databaseRef.child(userId);
            userRef.child("rating").setValue(rating+" star");
            userRef.child("feedback").setValue(feedback);

            Toast.makeText(this, "Feedback sent to the developers", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Heartrate.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
