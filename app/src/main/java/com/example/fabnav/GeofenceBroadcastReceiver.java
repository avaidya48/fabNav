package com.example.fabnav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiv";

    private Boolean isRedirectCancelled = false;
    GeofenceBroadcastReceiver receiver = this;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NotificationHelper notificationHelper = new NotificationHelper(context);

        geofencingClient = LocationServices.getGeofencingClient(context);
        geofenceHelper = new GeofenceHelper(context);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
//        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                if(geofenceList.get(0).getRequestId().equals("DESTINATION")){
                    Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("Entered Geofence. Rerouting..", "", MapsActivity.class, true);

                    long endWaitTime = System.currentTimeMillis() + 10*1000;
                    MyApp app = (MyApp) context.getApplicationContext();

                    //boolean isConditionMet = app.getCancelRedirect();
                /*while (System.currentTimeMillis() < endWaitTime) {
                    continue;
                }*/
                    AndroidNetworking.get("https://fabnav-backend.herokuapp.com/getSuggestedParking/")
                            .addHeaders("userName",app.getUser().getUserName())
                            .addHeaders("latitude", String.valueOf(app.getDestLat()))
                            .addHeaders("longitude", String.valueOf(app.getDestLong()))
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        app.setParkingId(response.get("id").toString());
                                        Double latitude = Double.valueOf(response.get("latitude").toString());
                                        Double longitude = Double.valueOf(response.get("longitude").toString());
                                        Toast.makeText(context,"Got Suggestion",Toast.LENGTH_LONG);
                                        //receiver.startGoogleNav(context, 33.77703460146702, -84.38964359921141);
                                        Thread.sleep(1000);
                                        receiver.startGoogleNav(context, latitude, longitude);
                                    } catch (JSONException | InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(ANError error) {
                                    Toast.makeText(context,"Got Suggestion",Toast.LENGTH_LONG);
                                }
                            });

                }else if(geofenceList.get(0).getRequestId().equals("PARKING")){

                    notificationHelper.sendHighPriorityNotification("Parking Survey", "", SurveyActivity.class, false);
                }


                break;
        }





    }


    private void startGoogleNav(Context context, double destLat, double destLong) {
        String uri = "google.navigation:q=" + destLat + "," + destLong + "&mode=d";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApp app = (MyApp) context.getApplicationContext();
        LatLng park = new LatLng(destLat, destLong);
        float parkGeofenceRadius = (float) 200;
        Log.d(TAG, "reached redirect req" );
        addGeofence(park, parkGeofenceRadius);
        context.startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geofenceHelper.getGeofence("PARKING", latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }

}