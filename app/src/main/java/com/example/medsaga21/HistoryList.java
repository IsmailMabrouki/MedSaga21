package com.example.medsaga21;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryList extends AppCompatActivity {

    private ArrayList<MedicalHistoryModel> medicalHistoryModelArrayList;
    private final ArrayList<String> dateList=new ArrayList<>();
    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;
    //   private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);

        GridView prescriptionList = (GridView) findViewById(R.id.prescriptionListGV);
        int drId = userAuthenticationSharedPreference.getDrId();

        DatabaseSource databaseSource = new DatabaseSource(this);
        medicalHistoryModelArrayList = new ArrayList<>();

        medicalHistoryModelArrayList = databaseSource.getAllMedicalHistory(drId);

//        Log.d("date", String.valueOf(medicalHistoryModelArrayList.get()));
        for(MedicalHistoryModel medicalHistoryModel:medicalHistoryModelArrayList){
            dateList.add(medicalHistoryModel.getDate());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1,dateList);
        prescriptionList.setAdapter(arrayAdapter);

        prescriptionList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(HistoryList.this, ShowMedicalHistory.class);
            int historyId = medicalHistoryModelArrayList.get(position).getId();
            intent.putExtra("history_id", historyId);
            Log.d("history_id_list", String.valueOf(historyId));
            startActivity(intent);
            finish();
        });



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

                Intent intent = new Intent(HistoryList.this,DrListActivity.class);
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
            Intent intent = new Intent(HistoryList.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }
}
