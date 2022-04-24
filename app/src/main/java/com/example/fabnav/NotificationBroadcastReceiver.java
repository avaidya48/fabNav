package com.example.fabnav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        MyApp app = (MyApp) context.getApplicationContext();
        app.setCancelRedirect(true);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}