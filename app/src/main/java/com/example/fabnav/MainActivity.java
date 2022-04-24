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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private boolean permissionGranted;
    MyApp app ;
    private Object JsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (MyApp) getApplicationContext();
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.get("https://fabnav-backend.herokuapp.com/getUserDetails/")
                .addHeaders("name","test1")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.toString(),User.class);
                        app.setUser(user);
                        TextView textViewToChange = (TextView) findViewById(R.id.pointsCollected);
                        textViewToChange.setText(user.getCompletedSurveys()*10+" pts");
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        setContentView(R.layout.activity_main);

        //AndroidNetworking.initialize(getApplicationContext());
//        getSupportActionBar().hide();
    }

    public void openMaps(View view) throws InterruptedException {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enableUserLocation();
        if(this.permissionGranted){
            //33.775507, -84.401116
            //Tech sq: 33.777073864682784, -84.39094838795614
            Double destLat = 33.775507;
            Double destLong = -84.401116;
            //33.776862064689524, -84.3906077474249
            app.setDestLat(destLat);
            app.setDestLong(destLong);
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("LatLng", new double[]{destLat,destLong});
            startActivity(intent);
        }else{
            Toast.makeText(this, "Need Permission...", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSettings(View view) throws InterruptedException {
        Intent intent = new Intent(this, SettingsActivity.class);
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
