package org.hse.android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssAntennaInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor light;
    private final String TAG="Timerbaeva";
    private static final int REQUEST_IMAGE_CAPTURE=1;
    private static final String PERMISSION = android.Manifest.permission.CAMERA;
    private static final int REQUEST_PERMISSION_CODE = 3;
    private static final String[] PERMISSIONS={android.Manifest.permission.CAMERA};
    private ImageView imageView;
    private EditText user_name;
    private String userName;
    private TextView sensorLight;
    List<Sensor> sensors;
    private ListView all_sensors_txt;

    public void checkPermission(){

        int permissionCheck= ActivityCompat.checkSelfPermission(this, PERMISSION);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
         if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)){
        //showExplanation("","",PERMISSION,REQUEST_PERMISSION_CODE);
         }
        else ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_PERMISSION_CODE);
         }
        else {dispatchTakePictureIntent(); Log.e(TAG, "CheckPermission");}
    }


    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void loadPhoto(){
        Glide.with(this).load(currentPhotoPath).into(imageView);
        imageView.animate().rotation(90);

    }
    private void loadUserName(){
        user_name.setText(userName);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try{
                photoFile=createImageFile();
            } catch(IOException ex){
                Log.e(TAG,"Create file",ex);
            }
            if(photoFile!=null){
                Uri photoURI = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID+".provider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                try{
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                    loadPhoto();
                }catch(ActivityNotFoundException e){
                    Log.e(TAG, "Start activity", e);
                }
            }

        }
    }
    private void getAllSensors(){
        List<String> listSensorType = new ArrayList();
        for (int i = 0; i < sensors.size(); i++) {
            listSensorType.add(sensors.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listSensorType);
       all_sensors_txt.setAdapter(adapter);
    }
    SharedPreferences sharedPreferences;
    //private TextView sensorLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors=sensorManager.getSensorList(Sensor.TYPE_ALL);
        light=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorLight=findViewById(R.id.sensorLight);
        View takePhoto=(Button)findViewById(R.id.takePhoto);
        View all_sensors=(Button)findViewById(R.id.all_sensors);
        all_sensors.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        imageView = findViewById(R.id.photo);
        user_name=findViewById(R.id.user_name);
        all_sensors_txt =findViewById(R.id.all_sensors_txt);
        sharedPreferences = getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float lux= sensorEvent.values[0];
        sensorLight.setText(Float.toString(lux));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,light,SensorManager.SENSOR_DELAY_NORMAL);
        if(sharedPreferences.contains("image"))
        {
            currentPhotoPath=sharedPreferences.getString("image",currentPhotoPath);
            loadPhoto();
        }
        else DisplayToast("Изображения нет");
        if(sharedPreferences.contains("user_name"))
        {
            userName=sharedPreferences.getString("user_name",userName);
            loadUserName();
        }
        else DisplayToast("Нет имени");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"Starting writing to shared preferences");
        SharedPreferences.Editor sharedPrefEditor=sharedPreferences.edit();
        userName=user_name.getText().toString();
        sharedPrefEditor.putString("user_name",userName);
        sharedPrefEditor.putString("image",currentPhotoPath);
        sharedPrefEditor.apply();
        sensorManager.unregisterListener(this);
    }
    public void DisplayToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.takePhoto:
               // DisplayToast("");
                checkPermission();
                break;
            case R.id.all_sensors:
                getAllSensors();
                break;
            default:
                break;
        }
    }
}