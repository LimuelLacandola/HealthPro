package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    TextView fullname, gender, bloodtype, address, contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView homebutton = findViewById(R.id.homebutton);
        ImageView logoutbtn = findViewById(R.id.logoutbtn);
        ImageView profileImage = findViewById(R.id.profile_image);


        fullname = findViewById(R.id.fullname);
        gender = findViewById(R.id.gender);
        bloodtype = findViewById(R.id.bloodtype);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        readUserData();
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
                        String user_fullname = dataSnapshot.child("fullname").getValue(String.class);
                        String user_gender = dataSnapshot.child("gender").getValue(String.class);
                        String user_bloodtype = dataSnapshot.child("bloodtype").getValue(String.class);
                        String user_address = dataSnapshot.child("address").getValue(String.class);
                        String user_contact = dataSnapshot.child("contact").getValue(String.class);

                        // Display the retrieved data in your TextViews
                        fullname.setText(user_fullname);
                        gender.setText(user_gender);
                        bloodtype.setText(user_bloodtype);
                        address.setText(user_address);
                        contact.setText(user_contact);
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
        Intent intent = new Intent(UserProfile.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
