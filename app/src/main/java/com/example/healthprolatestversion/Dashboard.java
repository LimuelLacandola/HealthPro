    package com.example.healthprolatestversion;


    import static com.example.healthprolatestversion.Alarm.ACTION_NOTIFY;

    import androidx.appcompat.app.ActionBar;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.cardview.widget.CardView;
    import androidx.core.app.NotificationCompat;

    import android.Manifest;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class Dashboard extends AppCompatActivity {

        private static final int PERMISSION_REQUEST_CODE = 1;
        private static final String CHANNEL_ID = "popup_notification_channel";
        private static final int NOTIFICATION_ID = 1;


        CardView bmi;
        CardView article;
        CardView appointment;
        CardView heartrate;
        CardView stepscount;
        CardView aboutus;

        TextView fullname;

        ImageView user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

            fullname = findViewById(R.id.dashboardfullname);
            heartrate = findViewById(R.id.heartrate);
            article = findViewById(R.id.article);
            appointment = findViewById(R.id.appointment);
            stepscount = findViewById(R.id.stepscount);
            bmi = findViewById(R.id.bmi);
            aboutus = findViewById(R.id.aboutus);
            user = findViewById(R.id.user);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }



            readUserData();

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

            // Check for permissions when the activity is created
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions();
            }

            bmi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, BMI.class);
                    startActivity(intent);
                    finish();
                }
            });

            article.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, Article.class);
                    startActivity(intent);
                    finish();
                }
            });

            appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, Alarm.class);
                    startActivity(intent);
                    finish();
                }
            });

            stepscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, burnpacer.class);
                    startActivity(intent);
                    finish();
                }
            });

            heartrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, Heartrate.class);
                    startActivity(intent);
                    finish();
                }
            });

            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, UserProfile.class);
                    startActivity(intent);
                    finish();
                }
            });

            aboutus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, Aboutus.class);
                    startActivity(intent);
                    finish();
                }
            });
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

                            // Display the retrieved data in your TextViews
                            fullname.setText(user_fullname);
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

        private void checkPermissions() {
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACTIVITY_RECOGNITION,
            };

            boolean allPermissionsGranted = true;

            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
        public void onBackPressed() {

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == PERMISSION_REQUEST_CODE) {
                boolean allPermissionsGranted = true;

                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }


            }


        }

    }
