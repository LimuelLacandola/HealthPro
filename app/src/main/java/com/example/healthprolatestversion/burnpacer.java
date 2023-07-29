package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class burnpacer extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;
    private TextView steps;

    TextView calories, kilometers, burnpacertip;
    ImageView stepsback, burnpacerhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burnpacer);

        steps = findViewById(R.id.steps);
        calories = findViewById(R.id.calories);
        kilometers = findViewById(R.id.kilometers);
        stepsback = findViewById(R.id.stepsback);
        burnpacertip = findViewById(R.id.burnpacertip);
        burnpacerhistory = findViewById(R.id.burnpacerhistory);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToFirebase();
            }
        });


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

//        resetSteps();
        loadData();
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(burnpacer.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        burnpacerhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(burnpacer.this, BurnpacerHistory.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveDataToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("burnpacerhistory");

            int currentSteps = totalSteps - previousTotalSteps;
            double burnedCalories = currentSteps * 0.04;
            int convertedBurnedCalories = (int) burnedCalories;
            int walkedKilometers = currentSteps / 1312;
            String currentDate = getCurrentDate();
            String currentTime = getCurrentTime();

            // Create a new data entry with a unique key
            DatabaseReference entryRef = userRef.push();
            entryRef.child("steps").setValue(currentSteps);
            entryRef.child("calories").setValue(convertedBurnedCalories);
            entryRef.child("kilometers").setValue(walkedKilometers);
            entryRef.child("date").setValue(currentDate);
            entryRef.child("time").setValue(currentTime);

            Toast.makeText(burnpacer.this, "Steps, Calories, and Kilometers saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
        Date currentTime = new Date();
        return timeFormat.format(currentTime);
    }

    protected void onResume(){
        super.onResume();

        if (stepSensor == null){
            Toast.makeText(this, "No sensor found", Toast.LENGTH_SHORT).show();
        }
        else {
            mSensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps-previousTotalSteps;
            steps.setText(String.valueOf(currentSteps));

            double burnedcalories = currentSteps * 0.04;
            int convertedburnedcalories = (int) burnedcalories;
            calories.setText(String.valueOf(convertedburnedcalories) + " cal");

            int walkedkilometers = currentSteps / 1312;
            kilometers.setText(String.valueOf(walkedkilometers) + " km");


            if (currentSteps == 0){
                burnpacertip.setText("Oops! Looks like you haven't taken any steps today. Remember, every step counts! Try to incorporate more physical activity into your day.");
            }

            else if (currentSteps < 1000){
                burnpacertip.setText("Don't worry, everyone starts somewhere! Even a few hundred steps can make a difference. Try to gradually increase your step count each day.");
            }

            else if (currentSteps >= 1000 & currentSteps < 2000){
                burnpacertip.setText("You're making progress! Keep building on your momentum. Try taking short walks throughout the day to reach the 2000 steps milestone.");
            }

            else if (currentSteps >= 2000 & currentSteps < 3000){
                burnpacertip.setText("Keep going! You're getting closer to 3000 steps. Take the opportunity to be more active. Every step brings you closer to your goals.");
            }

            else if (currentSteps >= 3000 & currentSteps < 4000){
                burnpacertip.setText("You're doing great! Aim for 4000 steps next time. Look for opportunities to incorporate more walking into your routine. You can do it!");
            }

            else if (currentSteps >= 4000 & currentSteps < 5000){
                burnpacertip.setText("Nice effort! You're well on your way to reaching 5000 steps. Try taking longer walks or increasing your daily activities to boost your step count.");
            }

            else if (currentSteps >= 5000){
            burnpacertip.setText("Good Job!, Continue being healthy!");
            }


        }
    }


//    private void resetSteps(){
//        steps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(burnpacer.this, "Long press to reset steps", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        steps.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                previousTotalSteps = totalSteps;
//                steps.setText("0");
//                saveData();
//                return true;
//            }
//        });
//    }

//    private void saveData(){
//        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("key1", String.valueOf(previousTotalSteps));
//        editor.apply();
//    }

    private void loadData(){
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        int savedNumber = (int) sharedPref.getFloat("key1", 0f);
        previousTotalSteps = savedNumber;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(burnpacer.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
