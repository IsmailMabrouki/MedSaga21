package com.example.medsaga21;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowDoctorDetails extends AppCompatActivity {

    private DatabaseSource databaseSource;

    private String nameSt;
    private String detailsSt;
    private String dateSt;
    private String phoneSt;
    private String emailSt;


    private int dr_id;
    private String drNameString;

    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor_details);

        TextView showDrName = (TextView) findViewById(R.id.showDrNameTV);
        TextView showDetails = (TextView) findViewById(R.id.showDrDetails);
        TextView showDate = (TextView) findViewById(R.id.showDate);
        TextView showPhone = (TextView) findViewById(R.id.showPhoneTV);
        TextView showEmail = (TextView) findViewById(R.id.showEmailTV);
        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);

        ArrayList<DrModel> drModelArrayList;
//        dr_id = getIntent().getIntExtra("dr_id",0);
        dr_id = userAuthenticationSharedPreference.getDrId();

        databaseSource = new DatabaseSource(this);
        drModelArrayList = databaseSource.getSingleDoctorDetails(dr_id);

        nameSt = drModelArrayList.get(0).getDrName();
        detailsSt = drModelArrayList.get(0).getDrDetails();
        dateSt = drModelArrayList.get(0).getDrAppointment();
        phoneSt = drModelArrayList.get(0).getDrPhone();
        emailSt = drModelArrayList.get(0).getDrEmail();

        showDrName.setText(nameSt);
        showDetails.setText(detailsSt);
        showDate.setText(dateSt);
        showPhone.setText(phoneSt);
        showEmail.setText(emailSt);


        drNameString = drModelArrayList.get(0).getDrName();

    }

    public void addMedicalHistory(View view) {
        Intent intent = new Intent(ShowDoctorDetails.this,AddMedicalHistory.class);
        intent.putExtra("dr_name",drNameString);
        startActivity(intent);
    }

    public void showAllPrescription(View view) {
        Intent intent = new Intent(ShowDoctorDetails.this,HistoryList.class);
        intent.putExtra("dr_name",drNameString);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.actionUpdate:

                intent = new Intent(ShowDoctorDetails.this,AddDoctor.class);
                intent.putExtra("dr_id",dr_id);
                intent.putExtra("dr_name",nameSt);
                intent.putExtra("dr_details",detailsSt);
                intent.putExtra("dr_date",dateSt);
                intent.putExtra("dr_phone",phoneSt);
                intent.putExtra("dr_email",emailSt);
                startActivity(intent);
                finish();
                return true;
            case R.id.actionDelete:
                logout();
                deleteDr();
                return true;
            case R.id.actionHome:

                intent = new Intent(ShowDoctorDetails.this,DrListActivity.class);
                startActivity(intent);
                return true;
            case R.id.actionLogout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {

        boolean status = userAuthenticationSharedPreference.cleanUser();
        if (status){
            Intent intent = new Intent(ShowDoctorDetails.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }
    private void deleteDr(){
        boolean status = databaseSource.deleteDoctor(dr_id);
        if (status){
            Intent intent = new Intent(ShowDoctorDetails.this,DrListActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "Doctor not delete", Toast.LENGTH_SHORT).show();
        }
    }

//    private void updateDrDetailes(){
//
//    }


}
