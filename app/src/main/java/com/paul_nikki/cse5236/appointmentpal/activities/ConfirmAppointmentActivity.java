package com.paul_nikki.cse5236.appointmentpal.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.paul_nikki.cse5236.appointmentpal.AppConfig;
import com.paul_nikki.cse5236.appointmentpal.Controllers.AppController;
import com.paul_nikki.cse5236.appointmentpal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfirmAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnConfirm;
    Button btnBackToCal;
    Button btnMap;
    TextView whereText;
    TextView whenText;
    String appointment;
    String locationName;
    String doctorName;
    String address;
    JSONArray ja;

    String TAG = "ConfirmAppointmentActivity";
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);

        //get appointment info from intent
        uuid = getIntent().getStringExtra("uuid");
        appointment = getIntent().getStringExtra("appointmentDate");
        doctorName =  getIntent().getStringExtra("DoctorName");
        locationName = getIntent().getStringExtra("LocationName");


        btnConfirm = (Button)findViewById(R.id.btn_confirmAppt);
        btnConfirm.setOnClickListener(this);
        btnBackToCal = (Button)findViewById(R.id.btn_backToCalender);
        btnBackToCal.setOnClickListener(this);
        btnMap = (Button)findViewById(R.id.btn_mapConfirm);
        btnMap.setOnClickListener(this);
        whenText = (TextView)findViewById(R.id.lbl_confirmWhen);
        whereText = (TextView)findViewById(R.id.lbl_confirmWhere);

        whenText.setText(appointment);
        whereText.setText(doctorName + "-" + locationName);
    }

    public void createAppt(){

        String tag_string_req = "createNewAppointment";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_NEW_APPOINTMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Appointment response: " + response);


                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("error");

                    // Check for error node in json
                    if (error.equals("0")) {
                        // we got a response successfully
                        Intent intent = new Intent(getBaseContext(), BaseAccountActivity.class);
                        intent.putExtra("uuid", uuid);
                        startActivity(intent);

                    } else {
                        // Error in creating appointment. Get the error message
                        String errorMsg = "error getting appointments";//jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to createappointment url
                Map<String, String> params = new HashMap<>();
                params.put("uuid", uuid);
                params.put("doctoremail", getIntent().getStringExtra("doctorEmail"));
                params.put("appointmentdate", appointment);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getLocationAddress(){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_LOCATIONS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG+"Location", response.toString());
                try {
                    ja = response.getJSONArray("Doctors");
                    int s = ja.length();
                    for (int i = 0; i < s; i++) {
                        JSONObject jsonobject = ja.getJSONObject(i);
                        String practicename = jsonobject.getString("practicename");
                        String addressDB = jsonobject.getString("address");
                        if(practicename.equals(locationName)) {
                            address = addressDB;
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

            }
        });

        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_confirmAppt:
                createAppt();
                intent = new Intent(this, BaseAccountActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_backToCalender:
                intent = new Intent(this, CalendarActivity.class);

                intent.putExtra("uuid", getIntent().getStringExtra("uuid"));
                intent.putExtra("doctorEmail", getIntent().getStringExtra("doctorEmail"));
                intent.putExtra("DoctorName", doctorName);
                intent.putExtra("LocationName", locationName);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_mapConfirm:
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("Location", address);
                intent.putExtra("OfficeName", locationName);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
