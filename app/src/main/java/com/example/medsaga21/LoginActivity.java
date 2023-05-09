package com.example.medsaga21;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;
    private EditText emailET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passET);
        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);

        String shareEmail = userAuthenticationSharedPreference.getUserEmail();
        String sharePass = userAuthenticationSharedPreference.getUserEmail();
//        if (!shareEmail.equals("No thing found") && !sharePass.equals("No thing found")){
//            Intent intent = new Intent(LoginActivity.this,DrListActivity.class);
//            startActivity(intent);
//        }
    }

    public void login(View view) {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        if (email.isEmpty() && password.isEmpty()){

        }else {

            if (userAuthenticationSharedPreference.getUserEmail().equals(email) && userAuthenticationSharedPreference.getUserPassword().equals(password)){
                Intent intent = new Intent(LoginActivity.this,DrListActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(this, "Email or Password Not Match", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void registration(View view) {
        Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
        startActivity(intent);
//        finish();
    }
}
