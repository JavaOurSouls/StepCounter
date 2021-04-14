package com.javaoursouls.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity
        extends AppCompatActivity
        implements SensorEventListener{

    private String TAG = "MyLogMessages";
    private SensorManager sensorManager;
    private Sensor Sensor_Step;
    private Sensor Sensor_Light;
    private Sensor Sensor_Accelerometer;
    private TextView Text_Steps;
    private TextView Text_Longitude;
    private TextView Text_Latitude;
    private TextView Text_Light;
    private TextView Text_AccelerometerX;
    private TextView Text_AccelerometerY;
    private TextView Text_AccelerometerZ;
    private int steps = 0;

    // TODO : extract
    private final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Text_Longitude.setText(getResources().getString(R.string.string_Longitude, location.getLongitude()));
                Text_Latitude.setText(getResources().getString(R.string.string_Latitude, location.getLatitude()));
                Log.d(TAG, "location change: registered");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate : -> ");
        setContentView(R.layout.activity_main);
        checkPermission();

        Text_Light = (TextView) findViewById(R.id.Light);
        // TODO : use array
        Text_AccelerometerX = (TextView) findViewById(R.id.ACCX);
        Text_AccelerometerY = (TextView) findViewById(R.id.ACCY);
        Text_AccelerometerZ = (TextView) findViewById(R.id.ACCZ);

        Text_Latitude = (TextView) findViewById(R.id.Latitude);
        Text_Longitude = (TextView) findViewById(R.id.Longitude);
        Text_Steps = (TextView) findViewById(R.id.Steps);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor_Step = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor_Light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor_Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int SensorType = event.sensor.getType();

        // TODO : outsource logic
        switch (SensorType) {
            case Sensor.TYPE_LIGHT:
                Log.d(TAG, "Lightsensor: changed");
                Text_Light.setText(getResources().getString(R.string.string_Light, event.values[0]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                Log.d(TAG, "Accelerometer: changed");
                Text_AccelerometerX.setText(getResources().getString(R.string.string_ACCX, event.values[0]));
                Text_AccelerometerY.setText(getResources().getString(R.string.string_ACCY, event.values[1]));
                Text_AccelerometerZ.setText(getResources().getString(R.string.string_ACCZ, event.values[2]));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                Log.d(TAG, "Stepcounter: changed");
                Text_Steps.setText(getResources().getString(R.string.string_Steps, steps++));
            default:
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // check location permissions
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED){

                Log.d(TAG, "onCreate check permission: starting location manager");
                LocationManager LM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 1.0f, listener);

            } else {
                // ask user for permission
                // ansonsten muss manuel über Einstellungen die Berechtigung erteilt werden
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            // hier oder bei permission check location updates?? -_> hier nicht möglich mit folgender syntax
            // Log.d(TAG, "onPermissionResult: starting location manager");
            // LocationManager LM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 1.0f, listener);

        } else {
            checkPermission();
        }
    }

    // ========================================================
    // lifecycle hooks
    @Override
    protected void onStart() {
        super.onStart();

        if (Sensor_Light != null) { // Listener: Activity, Sensor, Delay constant
            sensorManager.registerListener(this, Sensor_Light, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onStart: Light sensor registered.");
        }

        if (Sensor_Accelerometer != null) {
            sensorManager.registerListener(this, Sensor_Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onStart: Accelerometer sensor registered.");
        }


        if (Sensor_Step != null) {
            sensorManager.registerListener(this, Sensor_Step, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onStart: Step Counter sensor registered.");
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        Log.d(TAG, "onStop: sensors stoped.");
    }
}
