package com.example.andre.basicfitness;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.support.v4.app.NotificationCompatExtras;
import android.support.v4.app.NotificationManagerCompat;
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
import android.support.v4.app.NotificationCompat;

public class main_page1 extends AppCompatActivity  {

    private Button logout;  //declare button
    private FirebaseAuth firebaseAuth; //firebase
    private SensorManager sensorManager;  //declare sensorManager
    private Sensor stepSensor; //declare a stepSensor
    private SensorEventListener stepSensorListener;
    long steps = 0;
    public TextView distanceTraveled;

    public static final String CHANNEL_ID = "ChannelNoti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        createNotificationChannel();

        //milestoneNotification = new NotificationCompat.Builder(this);
       // milestoneNotification.setAutoCancel(true); //closes notification when  clicked
        //notificationManager = NotificationManagerCompat.from(this);

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
                    System.out.println(steps);
                    calculateDistance(steps);
                    getDistance();
                    mileStoneNotification1();
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
        /*System.out.println("Value of steps: " + steps);
        //calculateDistance(steps);
        //getDistance();*/
        //mileStoneNotification1();

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

    public float calculateDistance(long steps){
        float totalDistance = (float)(steps*31)/(float)12; //calculate distance in miles
        System.out.println("Total Distance: " + totalDistance);
        return totalDistance;
    }

    public void getDistance(){
        float totalDist = calculateDistance((long) steps);
        //System.out.println(totalDist);
        distanceTraveled.setText(String.valueOf(totalDist + "feet"));
    }

    private NotificationManagerCompat notificationManager;
    private static final int id = 12451;  //unique id to milestone Notification

    public void mileStoneNotification1(){

        float totalDistance = calculateDistance((long) steps);
        //System.out.println("This is the mile stone distance" + totalDistance);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        if(totalDistance == 1000){

            //build notification
            builder.setSmallIcon(R.drawable.notilogo);
            builder.setTicker("Congrats You Have Walk 1000 ft");
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("New Achievement!");
            builder.setContentText("Congrats on Your Achievement");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setAutoCancel(true);

            Intent intent = new Intent(this, main_page1.class);
            PendingIntent pt = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pt);

            //send notification
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            //NotificationManager nm  = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManagerCompat.notify(id, builder.build());


        }
        else if((totalDistance % 1000) == 0){
            //also build notification
            builder.setSmallIcon(R.drawable.notilogo);
            builder.setTicker("Congrats You Have Walk 1000 More ft");
            builder.setWhen(System.currentTimeMillis());
            builder.setContentTitle("New Achievement!");
            builder.setContentText("Congrats on Your Achievement");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setAutoCancel(true);

            Intent intent = new Intent(this, main_page1.class);
            PendingIntent pt = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pt);

            //send notification
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            //NotificationManager nm  = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManagerCompat.notify(id, builder.build());
        }

    }

    //Notification Channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MileStone";
            String description = "Milestone Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for Notification");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
