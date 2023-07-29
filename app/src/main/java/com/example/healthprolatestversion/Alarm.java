package com.example.healthprolatestversion;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "popup_notification_channel";
    public static final String ACTION_NOTIFY = "com.example.remind.ACTION_NOTIFY";

    private NotificationManager notificationManager;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private TimePicker timePicker;
    private Button setButton;
    private EditText editText;


    ImageView alarmback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Popup Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Popup notification channel");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(ACTION_NOTIFY);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        timePicker = findViewById(R.id.timePicker);
        setButton = findViewById(R.id.setButton);
        editText = findViewById(R.id.remindtxt);
        alarmback = findViewById(R.id.alarmback);

        alarmback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alarm.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotification();
                String time = timePicker.getHour() + ":" + timePicker.getMinute();
                Toast.makeText(Alarm.this, "Reminder set at: " + time, Toast.LENGTH_SHORT).show();


                // Get the current user's UID
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String uid = currentUser.getUid();

                // Reference the current user's UID and set the reminder text
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                String reminderText = editText.getText().toString();
                databaseRef.child("reminder").setValue(reminderText);

            }
        });
    }

    private void setNotification() {
        int hour, minute;

        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long notificationTime = calendar.getTimeInMillis();

        // If the selected time is in the past, add one day to the notification time
        if (notificationTime < System.currentTimeMillis()) {
            notificationTime += 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        }

        // Cancel any existing alarm
        cancelNotification();

        // Schedule the alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);
    }

    private void cancelNotification() {
        alarmManager.cancel(alarmIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ACTION_NOTIFY.equals(intent.getAction())) {
                showNotification(context);
            }
        }
    }

    public static void showNotification(Context context) {
        String text = Alarm.getInstance().editText.getText().toString();

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/lato");

        // Create an Intent for the NotificationService
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.setAction(ACTION_NOTIFY);
        serviceIntent.putExtra("text", text);
        context.startService(serviceIntent);

        // Create an Intent for the RemindActivity activity
        Intent intent = new Intent(context, Alarmopen.class);
        intent.putExtra("text", text); // Pass the "text" value to the intent
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Load the image from the drawable resource
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Health Pro")
                .setContentText("It's time to " + text)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to high
                .setCategory(NotificationCompat.CATEGORY_CALL) // Set the category to call (for Heads-Up)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Create a full screen intent
        Intent fullScreenIntent = new Intent(context, Alarmopen.class);
        fullScreenIntent.putExtra("text", text); // Pass the "text" value to the intent
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }





    private static Alarm instance;

    public Alarm() {
        instance = this;
    }

    public static Alarm getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelNotification();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Alarm.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}