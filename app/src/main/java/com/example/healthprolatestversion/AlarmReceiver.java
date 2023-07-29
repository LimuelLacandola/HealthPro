package com.example.healthprolatestversion;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Alarm.ACTION_NOTIFY.equals(intent.getAction())) {
            Alarm.showNotification(context);
        }
        }
    }

