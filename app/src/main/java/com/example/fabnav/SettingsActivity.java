package com.example.fabnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    MyApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        app  = (MyApp) getApplicationContext();

        EditText ratePref = (EditText) findViewById(R.id.ratePref);
        ratePref.setText(app.getUser().getPaymentMax().toString(), TextView.BufferType.EDITABLE);

        Switch disabilityPref = (Switch) findViewById(R.id.disabilityPref);
        disabilityPref.setChecked(app.getUser().getDisabilityFriendly());

        int occupancyId = getResourceId(String.valueOf(app.getUser().getOccupancyPreference()), "id", getPackageName());
        RadioButton occPref = (RadioButton) findViewById(occupancyId);
        occPref.setChecked(true);

    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void saveUserDetails(View view) throws InterruptedException, JSONException {
        Switch disabilityPref = (Switch) findViewById(R.id.disabilityPref);
        app.getUser().setDisabilityFriendly(disabilityPref.isChecked());

        EditText ratePref = (EditText) findViewById(R.id.ratePref);
        app.getUser().setPaymentMax(Integer.valueOf(ratePref.getText().toString()));

        RadioGroup occ = (RadioGroup) findViewById(R.id.occupancy);
        String val = getResources().getResourceName(occ.getCheckedRadioButtonId()).split("/")[1];
        app.getUser().setOccupancyPreference(User.occupancy.valueOf(val));

        String jsonInString = new Gson().toJson(app.getUser());
        JSONObject mJSONObject = new JSONObject(jsonInString);
        AndroidNetworking.put("https://fabnav-backend.herokuapp.com/updateUser/")
                .addJSONObjectBody(mJSONObject)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SettingsActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }

                    private Context getActivity() {
                        return null;
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
