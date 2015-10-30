package com.paul_nikki.cse5236.appointmentpal.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.paul_nikki.cse5236.appointmentpal.Appointment;
import com.paul_nikki.cse5236.appointmentpal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentsMainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    Button btnNewAppt;
    TextView lblApptNumGreeting;
    ListView apptListView;
    String[] apptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_main);

        btnNewAppt = (Button)findViewById(R.id.btn_newAppointment);
        btnNewAppt.setOnClickListener(this);
        lblApptNumGreeting = (TextView)findViewById(R.id.lbl_numberAppt);
        apptListView = (ListView)findViewById(R.id.listView);
        apptListView.setOnItemClickListener(this);
        GenerateApptListView();
        UpdateGreeting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointments_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void GenerateApptListView(){
        //Example Array until database is setup
        SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        //call web service, get appointments
        String strdate = "11-04-2015 9:00:00";
        try {
            Date dateAppt = dateformat2.parse(strdate);
            Appointment one = new Appointment(dateAppt, "Dr. Smith", "3401 Morse Crossing Road Columbus OH 43215");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Appointment one = new Appointment(apt, "Dr. Smith", "3501")
        apptList = new Appointment[] {};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, apptList);
        apptListView.setAdapter(arrayAdapter);
    }

    public void UpdateGreeting(){
        int numOfAppt = apptList.length;
        String greeting = String.format("You have %d Appointment(s)!", numOfAppt);
        lblApptNumGreeting.setText(greeting);
    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_newAppointment:
                intent = new Intent(this, NewAppointmentActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AppointmentActivity.class);
        intent.putExtra("ApptIndex", position);
        startActivity(intent);
    }

}