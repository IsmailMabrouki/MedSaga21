package com.example.medsaga21;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class ShowMedicalHistory extends AppCompatActivity {


    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medical_history);

        ImageView prescriptionIV = findViewById(R.id.showHistoryPrescriptionIV);
        TextView drName = findViewById(R.id.showHistoryDrNameTV);
        TextView details = findViewById(R.id.showHistoryDrDetailsTV);
        TextView date = findViewById(R.id.showHistoryDrDateTV);
        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);

        int historyId = getIntent().getIntExtra("history_id", 0);
        Log.d("history_id", String.valueOf(historyId));
        DatabaseSource databaseSource = new DatabaseSource(this);
        ArrayList<MedicalHistoryModel> medicalHistoryModelArrayList;
        medicalHistoryModelArrayList = databaseSource.getSingleMedicalHistoryDetails(historyId);

        drName.setText(medicalHistoryModelArrayList.get(0).getDrName());
        details.setText(medicalHistoryModelArrayList.get(0).getDetails());
        date.setText(medicalHistoryModelArrayList.get(0).getDate());


        // Assuming you have the file path as a string
        String filePath = medicalHistoryModelArrayList.get(0).getPrescription();

// Load the image using Glide
        Glide.with(this)
                .load(new File(filePath))
                .into(prescriptionIV);

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

                Intent intent = new Intent(ShowMedicalHistory.this,DrListActivity.class);
                startActivity(intent);
                finish();
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
            Intent intent = new Intent(ShowMedicalHistory.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }
}
