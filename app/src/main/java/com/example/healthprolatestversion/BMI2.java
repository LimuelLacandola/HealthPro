package com.example.healthprolatestversion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.time.LocalTime;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BMI2 extends AppCompatActivity {

    TextView mbmidisplay, magedisplay, mweightdisplay, mheightdisplay, mbmicategory, mgender, articleresult, timeTextView;
    Button mgotomain;
    Intent intent;

    ImageView mimageview;
    String mbmi;
    String category;
    float intbmi;

    String height;
    String weight;

    float intheight, intweight;

    RelativeLayout mbackground;

    FirebaseDatabase database;
    DatabaseReference usersRef;

    private TextView dateTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi2);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());

        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Manila"));
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        String currentTimeStr;
        if (hour >= 12) {
            if (hour > 12) {
                hour -= 12;
            }
            currentTimeStr = String.format("%02d:%02d PM", hour, minute);
        } else {
            currentTimeStr = String.format("%02d:%02d AM", hour, minute);
        }



        String currentDate = dateFormat.format(calendar.getTime());
        dateTextView.setText(currentDate);
        timeTextView.setText(currentTimeStr);


        getSupportActionBar().setElevation(0);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E1D1D"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersRef = database.getReference("users").child(userId);
        }

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\"></font>"));
        getSupportActionBar().setTitle("Result");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        intent = getIntent();
        mbmidisplay = findViewById(R.id.bmidisplay);
        mbmicategory = findViewById(R.id.bmicategorydispaly);
        mgotomain = findViewById(R.id.gotomain);
        mimageview = findViewById(R.id.imageview);
        mgender = findViewById(R.id.genderdisplay);
        mbackground = findViewById(R.id.contentlayout);
        articleresult =findViewById(R.id.articleresult);

        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");

        intheight = Float.parseFloat(height);
        intweight = Float.parseFloat(weight);

        intheight = intheight / 100;
        intbmi = intweight / (intheight * intheight);

        mbmi = String.format(Locale.getDefault(), "%.2f", intbmi);

        if (intbmi < 16) {
            mbmicategory.setText("Severe Thinness");
            mimageview.setImageResource(R.drawable.crosss);
            articleresult.setText("Establish a regular eating pattern with three main meals and nutritious snacks in between. Aim for consistent meal times to maintain a stable energy intake throughout the day.");
        } else if (intbmi < 16.9 && intbmi > 16) {
            mbmicategory.setText("Moderate Thinness");
            mimageview.setImageResource(R.drawable.warning);
            articleresult.setText("Stay hydrated by drinking enough water throughout the day. Water is essential for various bodily functions and can help support weight gain.");
        } else if (intbmi < 18.4 && intbmi > 17) {
            mbmicategory.setText("Mild Thinness");
            mimageview.setImageResource(R.drawable.warning);
            articleresult.setText(" Engage in regular physical activity to promote overall health and maintain a healthy weight. Incorporate a combination of cardiovascular exercises (such as brisk walking, cycling, or swimming) and strength training exercises (using weights or resistance bands) to help build lean muscle mass and improve body composition.");
        } else if (intbmi < 24.9 && intbmi > 18.5) {
            mbmicategory.setText("Normal");
            mimageview.setImageResource(R.drawable.ok);
            articleresult.setText("Maintaining a normal BMI is a great achievement! To continue leading a healthy lifestyle, focus on balanced nutrition, regular exercise, and stress management.");
        } else if (intbmi < 29.9 && intbmi > 25) {
            mbmicategory.setText("Overweight");
            mimageview.setImageResource(R.drawable.warning);
            articleresult.setText("Be mindful of portion sizes to avoid overeating. Use smaller plates and bowls, and pay attention to your body's hunger and fullness cues.");
        } else if (intbmi < 34.9 && intbmi > 30) {
            mbmicategory.setText("Obese Class I");
            mimageview.setImageResource(R.drawable.warning);
            articleresult.setText("Set realistic and achievable goals for weight loss and overall health improvement. Gradual and sustainable changes are more likely to lead to long-term success. Focus on making positive lifestyle changes rather than pursuing drastic weight loss.");
        } else {
            mbmicategory.setText("Obese Class II");
            mimageview.setImageResource(R.drawable.crosss);
            articleresult.setText("Consider seeking support from a healthcare professional or a registered dietitian who can guide you through a personalized weight loss plan.");
        }

        mgender.setText(intent.getStringExtra("gender"));
        mbmidisplay.setText(mbmi);

        mgotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store intbmi value in Firebase Realtime Database
                if (usersRef != null) {
                    DatabaseReference newEntryRef = usersRef.push(); // Generate a new unique key
                    newEntryRef.child("BMIresult").setValue(String.format(Locale.getDefault(), "%.2f", intbmi));
                    newEntryRef.child("BMICategory").setValue(mbmicategory.getText().toString());
                    newEntryRef.child("BMIDate").setValue(dateTextView.getText().toString());
                    newEntryRef.child("BMITime").setValue(timeTextView.getText().toString());
                }

                Intent intent1 = new Intent(getApplicationContext(), BMI.class);
                startActivity(intent1);
            }
        });


    }
}