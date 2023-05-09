package com.example.medsaga21;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;



public class DatabaseSource {
    private final DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DrModel drModel;

    public DatabaseSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open(){
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }
    public void close(){
        sqLiteDatabase.close();
    }

    public boolean addDoctor(DrModel drs){
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME,drs.getDrName());
        values.put(DatabaseHelper.COL_DETAILS,drs.getDrDetails());
        values.put(DatabaseHelper.COL_APPOINTMENT,drs.getDrAppointment());
        values.put(DatabaseHelper.COL_PHONE,drs.getDrPhone());
        values.put(DatabaseHelper.COL_EMAIL,drs.getDrEmail());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_DOCTOR,null,values);
        this.close();
        return id > 0;
    }



    public boolean addMedicalHistory(MedicalHistoryModel mhm){
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MEDICAL_HISTORY_DR_ID,mhm.getDr_id());
        values.put(DatabaseHelper.COL_MEDICAL_HISTORY_PRESCRIPTION_PICTURE,mhm.getPrescription());
        values.put(DatabaseHelper.COL_MEDICAL_HISTORY_DR_NAME,mhm.getDrName());
        values.put(DatabaseHelper.COL_MEDICAL_HISTORY_DETAILS,mhm.getDetails());
        values.put(DatabaseHelper.COL_MEDICAL_HISTORY_DATE,mhm.getDate());


        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_MEDICAL_HISTORY,null,values);
        this.close();
        return id > 0;
    }


    public ArrayList<DrModel> getAllDrInfo(){
        ArrayList<DrModel>drArrayList = new ArrayList<>();
        this.open();
        /*Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DatabaseHelper.TABLE_DOCTOR,null);*/

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_DOCTOR,null,null,null,null,null,null);
        cursor.moveToFirst();
        if(
                cursor.getCount() > 0){
            for(int i = 0; i < cursor.getCount(); i++){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_DR_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME));
                @SuppressLint("Range") String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DETAILS));
                @SuppressLint("Range") String appointment = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_APPOINTMENT));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PHONE));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL));

                drModel = new DrModel(id,name,details,appointment,phone,email);
                drArrayList.add(drModel);
                cursor.moveToNext();
            }
        }
        cursor.close();
        this.close();
        return drArrayList;

    }


    @SuppressLint({"Range", "SuspiciousIndentation"})
    public ArrayList<MedicalHistoryModel> getAllMedicalHistory(int drId) {
        ArrayList<MedicalHistoryModel> medicalHistoryArrayList = new ArrayList<>();
        this.open();

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_MEDICAL_HISTORY, null,
                DatabaseHelper.COL_MEDICAL_HISTORY_DR_ID + " =? ", new String[]{String.valueOf(drId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_ID);
                int dr_idIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DR_ID);
                int prescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_PRESCRIPTION_PICTURE);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DR_NAME);
                int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DETAILS);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DATE);

                int id = cursor.getInt(idIndex);
                int dr_id = cursor.getInt(dr_idIndex);
                String prescription = (prescriptionIndex >= 0) ? cursor.getString(prescriptionIndex) : "";
                String name = (nameIndex >= 0) ? cursor.getString(nameIndex) : "";
                String details = (detailsIndex >= 0) ? cursor.getString(detailsIndex) : "";
                String date = (dateIndex >= 0) ? cursor.getString(dateIndex) : "";

                MedicalHistoryModel medicalHistoryModel = new MedicalHistoryModel(id, dr_id, prescription, name, details, date);
                medicalHistoryArrayList.add(medicalHistoryModel);
            }
            cursor.close();
        }

        this.close();
        return medicalHistoryArrayList;
    }




    public ArrayList<MedicalHistoryModel> getSingleMedicalHistoryDetails(int history_id) {
        ArrayList<MedicalHistoryModel> medicalHistoryArrayList = new ArrayList<>();
        this.open();

        try (Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_MEDICAL_HISTORY, null,
                DatabaseHelper.COL_MEDICAL_HISTORY_ID + " =? ", new String[]{String.valueOf(history_id)},
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_ID);
                int drIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DR_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DR_NAME);
                int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DETAILS);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_DATE);
                int prescriptionIndex = cursor.getColumnIndex(DatabaseHelper.COL_MEDICAL_HISTORY_PRESCRIPTION_PICTURE);

                while (!cursor.isAfterLast()) {
                    int id = (idIndex >= 0) ? cursor.getInt(idIndex) : 0;
                    int dr_id = (drIdIndex >= 0) ? cursor.getInt(drIdIndex) : 0;
                    String name = (nameIndex >= 0) ? cursor.getString(nameIndex) : "";
                    String details = (detailsIndex >= 0) ? cursor.getString(detailsIndex) : "";
                    String date = (dateIndex >= 0) ? cursor.getString(dateIndex) : "";
                    String prescription = (prescriptionIndex >= 0) ? cursor.getString(prescriptionIndex) : "";

                    MedicalHistoryModel medicalHistoryModel = new MedicalHistoryModel(id,dr_id, prescription, name, details, date);
                    medicalHistoryArrayList.add(medicalHistoryModel);

                    cursor.moveToNext();
                }
            }
        } finally {
            this.close();
        }

        return medicalHistoryArrayList;
    }




    public ArrayList<DrModel> getSingleDoctorDetails(long dr_id){
        ArrayList<DrModel> doctorModelArrayList= new ArrayList<>();

        this.open();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_DOCTOR, null, DatabaseHelper.COL_DR_ID+" =? ",new String[] { String.valueOf(dr_id) },null,null,null);
        cursor.moveToFirst();

                @SuppressLint("Range") int Dr_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_DR_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME));
                @SuppressLint("Range") String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DETAILS));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_APPOINTMENT));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PHONE));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL));

                drModel = new DrModel(name,details,date,phone,email);
                doctorModelArrayList.add(drModel);




        cursor.close();
        this.close();

        return doctorModelArrayList;
    }




    // -----------------DELETE--------------------//


    public boolean deleteDoctor(long dr_id){
        this.open();
        int deleteDrId = sqLiteDatabase.delete(DatabaseHelper.TABLE_DOCTOR,DatabaseHelper.COL_DR_ID+" = ?",new String[]{Long.toString(dr_id)});

        return deleteDrId > 0;
    }



    // -----------------DELETE--------------------//


    public boolean updateDrDetailes(DrModel drs,long dr_id){
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME,drs.getDrName());
        values.put(DatabaseHelper.COL_DETAILS,drs.getDrDetails());
        values.put(DatabaseHelper.COL_APPOINTMENT,drs.getDrAppointment());
        values.put(DatabaseHelper.COL_PHONE,drs.getDrPhone());
        values.put(DatabaseHelper.COL_EMAIL,drs.getDrEmail());


        long id = sqLiteDatabase.update(DatabaseHelper.TABLE_DOCTOR,values,DatabaseHelper.COL_DR_ID+" = ?",new String[]{Long.toString(dr_id)});
        this.close();
        return id > 0;
    }


}
