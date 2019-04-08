package com.example.andre.basicfitness;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class main_page1 extends AppCompatActivity {

    //Initialization of Components
    public EditText userWork;
    private Button logout;  //declare button
    private FirebaseAuth firebaseAuth; //firebase
    private SensorManager sensorManager;  //declare sensorManager
    private Sensor stepSensor; //declare a stepSensor
    private SensorEventListener stepSensorListener; //initializes stepListener
    long steps = 0;  //initializes step count
    public TextView distanceTraveled; //Textview to display Distance Traveled
    private LocationManager locationManager;  //Initialize Location manager
    private LocationListener locationListener;
    private boolean b = false;




    /*String userAddress = userWork.getText().toString().trim();

    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    List<Address> addresses;

    {
        try {
            addresses = geocoder.getFromLocationName(userAddress, 1);
            Address address = addresses.get(0);
            if(addresses.size() > 0) {
                double latitudeU = addresses.get(0).getLatitude();
                double longitudeU = addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
   /*LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();  //get latitude of user
            double lon = location.getLongitude();  //get longitude of user

            float [] results = new float[1];

            String userAddress = userWork.getText().toString().trim();
            Location.distanceBetween(lat, lon, addresses.get(0).getLatitude(), addresses.get(0).getLongitude(),results);
            float dist = results[0];

            if(dist < 0.1){
                b = true;
                reminderToWalk();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        //checks if GPS is turned off
        @Override
        public void onProviderDisabled(String provider) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
    };*/

    public static final String CHANNEL_ID = "ChannelNoti"; //CHANNEl_ID for milestones
    public static final String CHANNEL_ID1 = "Work";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        createNotificationChannel();
        createNotificationWorkChannel();

        firebaseAuth = FirebaseAuth.getInstance();  //firebase

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  //initializes sensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR); //initializes step Senser
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //initializes a Location Manager
        locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();  //get latitude of user
                double lon = location.getLongitude();  //get longitude of user
                float [] results = new float[1];


                //String userAddress = userWork.getText().toString().trim();
                Location.distanceBetween(lat, lon, getLociLat() , getLociLong(),results);
                float dist = results[0];

                if(dist < 0.1){
                    b = true;
                    reminderToWalk();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            //checks if GPS is turned off
            @Override
            public void onProviderDisabled(String provider) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        locationManager.requestLocationUpdates("gps", 5000, (float) 5.0, locationListener);

        //permission user check
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                } ,10);
                return;
            }

            else{
                configureButton();
            }

        }
        else{
            configureButton();
        }

        //onRequestPermissionsResult();
        //locationManager.requestLocationUpdates("gps", 5000, (float) 5.0, locationListener);

        //if user is not signed in
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(main_page1.this, MainActivity.class);
            main_page1.this.startActivity(intent);
        }
        //finds button
        logout = (Button) findViewById(R.id.logout);
        //creates listener for LogOut Button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(main_page1.this, MainActivity.class);
                main_page1.this.startActivity(intent);
        }
        });

        distanceTraveled = (TextView) findViewById((R.id.distance));  //finds TextView

    }



    //End of Bundle Instances
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissiond[], int[] grantResults) {
        switch (requestCode){
            case  10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                    return;
                }
        }
    }

    private void configureButton() {
        locationManager.requestLocationUpdates("gps", 5000, (float) 5.0, locationListener);
    }

    //Handles stop and resume walking for Step Sensor
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

    //Calculate total Distance Traveled
    public float calculateDistance(long steps){
        float totalDistance = (float)(steps*31)/(float)12; //calculate distance in miles
        System.out.println("Total Distance: " + totalDistance);
        return totalDistance;
    }

    //Display Distance Traveled on MainScreen
    public void getDistance(){
        float totalDist = calculateDistance((long) steps);
        //System.out.println(totalDist);
        distanceTraveled.setText(String.valueOf(totalDist + "feet"));
    }

    //Create NotificationManager
    private NotificationManagerCompat notificationManager;
    private static final int id = 12451;  //unique id for milestone Notification

    public void mileStoneNotification1(){

        float totalDistance = calculateDistance((long) steps);
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

    //Create Work Notification Channel
    private void createNotificationWorkChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Work";
            String description = "Remidner to Walk";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Remind User to walk Around");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void reminderToWalk(){
        //TODO
        //use AlarmManager Library
        //Create notification

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        PendingIntent pt = PendingIntent.getBroadcast(getApplicationContext(), 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pt);

        Calendar calendar = Calendar.getInstance();
        int currentHourIn24Format = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinIn24Format = calendar.get(Calendar.MINUTE);
        int currentSecIn24Format = calendar.get(Calendar.SECOND);
        calendar.set(Calendar.HOUR_OF_DAY, currentHourIn24Format);
        calendar.set(Calendar.MINUTE, currentMinIn24Format);
        calendar.set(Calendar.SECOND, currentSecIn24Format);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); //intialize alarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pt);
    }

    private Boolean validate(){
        Boolean initial = false;

        userWork = (EditText)findViewById(R.id.workLocation);

        String userAddress= userWork.getText().toString().trim();

        if( userAddress.isEmpty() )
        {
            Toast.makeText(this,"Please Enter Your Work Location",Toast.LENGTH_SHORT).show();
        }

        else
        {
            initial = true;
        }

        return initial;
    }

    public double getLociLat(){

        if(validate()){
        //String userAddress = userWork.getText().toString().trim();
        //double latitudeU;
        //double longitudeU;

        userWork = (EditText)findViewById(R.id.workLocation);

        String userAddress= userWork.getText().toString().trim();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double latitudeU;
        double longitudeU;
        List<Address> addresses;

        {
            try {
                addresses = geocoder.getFromLocationName(userAddress, 1);
                Address address = addresses.get(0);
                if(addresses.size() > 0) {
                    latitudeU = addresses.get(0).getLatitude();
                    return latitudeU;

                }
            } catch (IOException e) {
                e.printStackTrace();
                return 0.0;
            }
        }}
    return 0.0;
    }

    public double getLociLong(){
        //String userAddress = userWork.getText().toString().trim();
        //double latitudeU;
        //double longitudeU;
    if(validate()){
        userWork = (EditText)findViewById(R.id.workLocation);

        String userAddress= userWork.getText().toString().trim();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double latitudeU;
        double longitudeU;
        List<Address> addresses;

        {
            try {
                addresses = geocoder.getFromLocationName(userAddress, 1);
                Address address = addresses.get(0);
                if(addresses.size() > 0) {
                    longitudeU = addresses.get(0).getLongitude();
                    return longitudeU;

                }
            } catch (IOException e) {
                e.printStackTrace();
                return 0.0;
            }
        }}
        return 0.0;
    }

}
