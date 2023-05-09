package com.example.medsaga21;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;



public class DrListActivity extends AppCompatActivity {

    private ArrayList<DrModel> drModelArrayList;
    private DatabaseSource databaseSource;
    private DrAdapter drAdapter;
    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;

    private int drPosition;

    private long dr_id;


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_list);
        ListView drListLV = (ListView) findViewById(R.id.drLV);
        new DrModel();
        databaseSource = new DatabaseSource(this);
        drModelArrayList = new ArrayList<>();

        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(DrListActivity.this);


        drModelArrayList = databaseSource.getAllDrInfo();
        drAdapter = new DrAdapter(this,drModelArrayList);
        drListLV.setAdapter(drAdapter);



        drListLV.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(DrListActivity.this,ShowDoctorDetails.class);
            userAuthenticationSharedPreference.setDrId(databaseSource.getAllDrInfo().get(position).getDrId());
            startActivity(intent);
        });
        drListLV.setOnItemLongClickListener((parent, view, position, id) -> {
            dr_id=databaseSource.getAllDrInfo().get(position).getDrId();
            drPosition=position;
            registerForContextMenu(parent);
            openContextMenu(parent);
            return true;
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.android_floating_action_button_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(DrListActivity.this, AddDoctor.class);
            startActivity(intent);
        });










    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_floating_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        item.getMenuInfo();
        if (item.getItemId() == R.id.floatingDelete) {
            deleteDr(dr_id, drPosition);
//                Toast.makeText(getApplicationContext(), "id no", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void logout(View view) {
        logout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_list_action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.actionLogout) {
            logout();
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        boolean status = userAuthenticationSharedPreference.cleanUser();
        if (status){
            Intent intent = new Intent(DrListActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteDr(long dr_id, int position){
        boolean status = databaseSource.deleteDoctor(dr_id);
        if (status){

            drModelArrayList.remove(position);
            drAdapter.notifyDataSetChanged();


            Toast.makeText(this, "Doctor delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Doctor not delete", Toast.LENGTH_SHORT).show();
        }
    }





}
