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

public class SurveyActivity extends AppCompatActivity {

    MyApp app;
    String parkingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        app  = (MyApp) getApplicationContext();
        app.setSurvey(new Survey());
        app.getSurvey().setParkingId(app.getParkingId());

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

    public void submitSurvey(View view) throws InterruptedException, JSONException {
        Switch safety = (Switch) findViewById(R.id.surveySafety);
        app.getSurvey().setSafety(safety.isChecked());

        EditText rate = (EditText) findViewById(R.id.surveyRate);
        app.getSurvey().setRate(Long.valueOf(rate.getText().toString()));

        RadioGroup occ = (RadioGroup) findViewById(R.id.surveyOccupancy);
        String val = getResources().getResourceName(occ.getCheckedRadioButtonId()).split("/")[1].split("survey")[1];
        app.getSurvey().setOccupancy(User.occupancy.valueOf(val));

        app.getSurvey().setUserName(app.getUser().getUserName());

        String jsonInString = new Gson().toJson(app.getSurvey());
        JSONObject mJSONObject = new JSONObject(jsonInString);
        AndroidNetworking.put("https://fabnav-backend.herokuapp.com/survey/")
                .addJSONObjectBody(mJSONObject)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SurveyActivity.this, "Thank You!", Toast.LENGTH_LONG).show();
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
