package com.example.medsaga21;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class AddMedicalHistory extends AppCompatActivity {

    private MedicalHistoryModel medicalHistoryModel;
    private DatabaseSource databaseSource;
    private UserAuthenticationSharedPreference userAuthenticationSharedPreference;


    private ImageView prescriptionIV;
    private EditText drNameET;
    private EditText detailsET;
    private EditText dateET;


    private Calendar calendar;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private Uri fileUri;            // file url to store image/video







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_history);
        prescriptionIV = (ImageView) findViewById(R.id.prescriptionIV);
        Button prescriptionBtn = (Button) findViewById(R.id.prescriptionBtn);
        drNameET = (EditText) findViewById(R.id.doctorNameET);
        detailsET = (EditText) findViewById(R.id.detailsET);
        dateET = (EditText) findViewById(R.id.dateET);
        Button historyBtn = (Button) findViewById(R.id.historyBtn);
        databaseSource = new DatabaseSource(this);
        userAuthenticationSharedPreference = new UserAuthenticationSharedPreference(this);
        calendar = Calendar.getInstance(Locale.getDefault());

        final int dr_id = userAuthenticationSharedPreference.getDrId();
        String drNameString = getIntent().getStringExtra("dr_name");

        drNameET.setText(drNameString);



        dateET.setOnClickListener(v -> {

            final int year=calendar.get(Calendar.YEAR);
            final int month=calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DAY_OF_MONTH);



            DatePickerDialog datePickerDialog=new DatePickerDialog(AddMedicalHistory.this, (view, year1, month1, dayOfMonth) -> {

                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH, month1 +1);
                calendar.set(Calendar.YEAR, year1);
//                        String dateString = sdf.format(calendar.getTimeInMillis());
                dateET.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR));

            },year,month,date);
            datePickerDialog.show();
        });


        prescriptionBtn.setOnClickListener(v -> captureImage());


        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }


        historyBtn.setOnClickListener(v -> {

//                if (drNameET.isEm)

            String prescription = fileUri.toString();
            String name = drNameET.getText().toString().trim();
            String historyDetails = detailsET.getText().toString().trim();
            String date = dateET.getText().toString().trim();


            int id = getIntent().getIntExtra("id",0);
            medicalHistoryModel = new MedicalHistoryModel(id,dr_id,prescription,name,historyDetails,date);
            boolean status = databaseSource.addMedicalHistory(medicalHistoryModel);
            if(status){
                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddMedicalHistory.this,ShowDoctorDetails.class));
                finish();

            }else{
                Toast.makeText(getApplicationContext(), "Could not save", Toast.LENGTH_SHORT).show();
            }


        });



    }

    private void captureImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "my_image.jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Note: do not set the DATA field when inserting into MediaStore, let the system handle it
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = uri;

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }




    private boolean isDeviceSupportCamera() {
        // this device has a camera
        // no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }



    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image capture was successful, get the file path from the Uri
                String filePath = fileUri.getPath();
                Toast.makeText(this, "Image captured at: " + filePath, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, check resultCode for details
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {


            prescriptionIV.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            prescriptionIV.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_medical_history_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.historyActionHome:

                Intent intent = new Intent(AddMedicalHistory.this,DrListActivity.class);
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
            Intent intent = new Intent(AddMedicalHistory.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Logout not successfully", Toast.LENGTH_SHORT).show();
        }

    }



}
