package com.example.medsaga21;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddDoctor extends AppCompatActivity {

    private Calendar calendar;

    private EditText drNameET;
    private EditText drDetailsET;
    private TextView drAppointmentTV;
    private EditText drPhoneET;
    private EditText drEmailET;

    private DrModel drModel;
    private DatabaseSource databaseSource;
    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;

    private int dr_id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        drNameET = (EditText) findViewById(R.id.drName);
        drDetailsET = (EditText) findViewById(R.id.drDetails);
        drAppointmentTV = (TextView) findViewById(R.id.drAppointment);
        drPhoneET = (EditText) findViewById(R.id.drPhone);
        drEmailET = (EditText) findViewById(R.id.drEmail);
        Button addDrBtn = (Button) findViewById(R.id.addDrBtn);
        calendar = Calendar.getInstance(Locale.getDefault());

        databaseSource = new DatabaseSource(this);
        drModel = new DrModel();

        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);

        drAppointmentTV.setOnClickListener(v -> {

            final int year=calendar.get(Calendar.YEAR);
            final int month=calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DAY_OF_MONTH);



            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog=new DatePickerDialog(AddDoctor.this, (view, year1, month1, dayOfMonth) -> {

                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH, month1 +1);
                calendar.set(Calendar.YEAR, year1);
//                        String dateString = sdf.format(calendar.getTimeInMillis());
                drAppointmentTV.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR));

            },year,month,date);
            datePickerDialog.show();
        });


        dr_id = getIntent().getIntExtra("dr_id",0);
        String dr_name = getIntent().getStringExtra("dr_name");
        String dr_details = getIntent().getStringExtra("dr_details");
        getIntent().getStringExtra("dr_date");
        String dr_phone = getIntent().getStringExtra("dr_phone");
        String dr_email = getIntent().getStringExtra("dr_email");


        drNameET.setText(dr_name);
        drDetailsET.setText(dr_details);
//        drAppointmentTV.setText(dr_date);
        drPhoneET.setText(dr_phone);
        drEmailET.setText(dr_email);

        if(dr_id!=0){
            addDrBtn.setText("Update");
        }

    }

    public void addDoctor(View view) {
        String name = drNameET.getText().toString().trim();
        String details = drDetailsET.getText().toString().trim();
        String appointment = drAppointmentTV.getText().toString().trim();
        String phone = drPhoneET.getText().toString().trim();
        String email = drEmailET.getText().toString().trim();


        if (dr_id!=0){

            drModel = new DrModel(dr_id, name, details, appointment, phone, email);
            boolean status = databaseSource.updateDrDetailes(drModel,dr_id);
            if(status){
                Toast.makeText(this, "Doctor Profile Update", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddDoctor.this,ShowDoctorDetails.class));
                finish();
            }else{
                Toast.makeText(this, "Could not Update", Toast.LENGTH_SHORT).show();
            }

        }else {

            drModel = new DrModel(name, details, appointment, phone, email);
            boolean status = databaseSource.addDoctor(drModel);
            if(status){
                Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddDoctor.this,DrListActivity.class));
                finish();
            }else{
                Toast.makeText(this, "Could not save", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_medical_history_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.historyActionHome:

                Intent intent = new Intent(AddDoctor.this,DrListActivity.class);
                startActivity(intent);
                return true;


            case R.id.historyActionLogout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {

        boolean status = userAuthenticationSharedPreference.cleanUser();
        if (status){
            Intent intent = new Intent(AddDoctor.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }
}
