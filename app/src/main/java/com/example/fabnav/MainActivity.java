package com.example.fabnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private boolean permissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
    }

    public void openMaps(View view) throws InterruptedException {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enableUserLocation();
        if(this.permissionGranted){
            double destLat = 33.7774382;
            double destLong = -84.3995084;
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("LatLng", new double[]{destLat,destLong});
            startActivity(intent);
            startGoogleNav(destLat, destLong);
//            TimeUnit.SECONDS.sleep(10);
//            double destLat2 = 33.77985451688092;
//            double destLong2 = -84.3888850454161;
//            NotificationHelper notificationHelper = new NotificationHelper(this);
//            notificationHelper.sendHighPriorityNotification("Rerouting", "", MapsActivity.class);
//            startGoogleNav(destLat2, destLong2);
        }else{
            Toast.makeText(this, "Need Permission...", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSettings(View view) throws InterruptedException {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startGoogleNav(double destLat, double destLong) throws InterruptedException {
        String uri = "google.navigation:q=" + destLat + "," + destLong + "&mode=d";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.permissionGranted =  true;
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                this.permissionGranted = true;
            } else {
                //We do not have the permission..

            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
