package com.example.andre.basicfitness;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1  = new Intent(context, main_page1.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        //Builds Notification to Remind User to Walk Around Every Hour
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, main_page1.CHANNEL_ID1);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.notilogo);
        builder.setContentTitle("Get Active!!");
        builder.setContentText("Walk Around for 5 Minutes");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(100, builder.build());
    }
}
