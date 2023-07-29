package com.example.healthprolatestversion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Alarmopen extends AppCompatActivity {

    TextView remindopen, greettxt;
    Button remindback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmopen);

        greettxt = findViewById(R.id.greettxt);
        remindback = findViewById(R.id.remindback);
        remindopen = findViewById(R.id.remindopen);

        readUserData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();

            remindback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Alarmopen.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void readUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String full_name = dataSnapshot.child("fullname").getValue(String.class);
                        String reminder = dataSnapshot.child("reminder").getValue(String.class);

                        // Display the retrieved data in your TextViews

                      greettxt.setText("Hello, " + full_name);
                        remindopen.setText("It's time to " + reminder);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that occur during the data retrieval
                    Log.e("UserProfile", "Failed to read user data.", databaseError.toException());
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Alarmopen.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
