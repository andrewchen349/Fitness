package com.example.andre.basicfitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;

public class main_page1 extends AppCompatActivity  {

    private Button logout;  //declare button
    private FirebaseAuth firebaseAuth; //firebase
    private SensorManager sensorManager;  //declare sensorManager
    private Sensor stepSensor; //declare a stepSensor
    private SensorEventListener stepSensorListener;
    private long steps = 0;
    public TextView distanceTraveled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);

        firebaseAuth = FirebaseAuth.getInstance();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  //initializes sensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR); //intializes step Senser
        stepSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                float[] values = event.values;
                int value = -1;

                if (values.length > 0) {
                    value = (int) values[0];
                }


                if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    steps++;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //if user is not signed in
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(main_page1.this, MainActivity.class);
            main_page1.this.startActivity(intent);
        }

        //finds button
        logout = (Button) findViewById(R.id.logout);

        //creates listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(main_page1.this, MainActivity.class);
                main_page1.this.startActivity(intent);
        }
        });

        distanceTraveled = (TextView) findViewById((R.id.distance));
        getDistance();

    }

    @Override
    protected void onResume() {

        super.onResume();

        sensorManager.registerListener(stepSensorListener, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(stepSensorListener);
    }

    private float calculateDistance(long steps){
        float totalDistance = (float)(steps*31)/(float)63360; //calculate distance in miles
        return totalDistance;
    }

    private void getDistance(){
        float totalDist = calculateDistance((long) steps);
        System.out.println(totalDist);
        distanceTraveled.setText(String.valueOf(totalDist));
    }
}
