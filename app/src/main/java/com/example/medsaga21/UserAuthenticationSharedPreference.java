package com.example.medsaga21;

import android.content.Context;
import android.content.SharedPreferences;



public class UserAuthenticationSharedPreference {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String USER_EMAIL="email";
    private static final String USER_PASSWORD="password";
    private static final String DEFAULT_MSG="No thing found";
    private static final String PREFERENCE_NAME="user authentication";
    private static final String DR_ID="dr_id";



    public UserAuthenticationSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean saveUser(String email,String pass){
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_PASSWORD,pass);
        editor.commit();
        return !USER_EMAIL.isEmpty() || !USER_PASSWORD.isEmpty();

    }
    public boolean setDrId(int id){
        editor.putInt(DR_ID,id);
        editor.commit();
        return !DR_ID.isEmpty();
    }
    public boolean cleanUser(){
        editor.clear();
        editor.commit();
        return true;
    }
    public String getUserEmail(){
        return sharedPreferences.getString(USER_EMAIL,DEFAULT_MSG);
    }

    public String getUserPassword(){
        return sharedPreferences.getString(USER_PASSWORD,DEFAULT_MSG);
    }
    public int getDrId(){
        return sharedPreferences.getInt(DR_ID,0);
    }


}
