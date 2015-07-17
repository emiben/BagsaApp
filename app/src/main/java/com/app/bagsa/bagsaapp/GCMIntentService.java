package com.app.bagsa.bagsaapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.app.bagsa.bagsaapp.Utils.DBHelper;
import com.app.bagsa.bagsaapp.Utils.Env;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by SBT on 13/07/2015.
 */
public class GCMIntentService extends IntentService{
    private static final int NOTIF_ALERTA_ID = 1;
    private int countNotifications = 0;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                mostrarNotification(extras.getString("msg"));
                insertNotifMsg(extras.getString("msg"));
            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void mostrarNotification(String msg)
    {
        countNotifications = Env.getNotificationsCount()+1;
        Env.setNotificationsCount(countNotifications);
        String[] m = new String[4];
        String mje = "";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try{
            m = msg.split("#");
            mje = m[1];
        }catch (Exception e){

        }
        if(mje.equals("")){
            mje = msg;
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notif_calend)
                        .setContentTitle("Bagsa")
                        .setContentText(mje)
                        .setAutoCancel(true)
                        .setNumber(countNotifications)
                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        Intent notIntent =  new Intent(this, NotificationsActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        setNumberOfNotifications();

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());


    }

    private void setNumberOfNotifications() {

        int badgeCount = 0;
        try {
            badgeCount = Env.getNotificationsCount();
            ShortcutBadger.with(getApplicationContext()).count(badgeCount);
        } catch (NumberFormatException e) {
            //Toast.makeText(getApplicationContext(), "Error input", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.getMessage();
        }

        //ShortcutBadger.setBadge(getApplicationContext(), badgeCount);
        //Toast.makeText(getApplicationContext(), "Set count=" + badgeCount, Toast.LENGTH_SHORT).show();
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

       // TextView textViewHomePackage = (TextView) findViewById(R.id.eTxtUserName);
       // textViewHomePackage.setText("launcher:" + currentHomePackage);
    }

    private String getDateString() {
        String[] fecha = new Timestamp(System.currentTimeMillis()).toString().split("-");
        //Formato fecha YYYYMMdd
        String fechaArch = fecha[0]+"-"+fecha[1]+"-"+fecha[2].substring(0, 2);
        return fechaArch;
    }

    public void insertNotifMsg(String msg) {
        DBHelper db = null;
        try {
            db = new DBHelper(getApplicationContext());
            db.openDB(1);
            //String fec = Calendar.getInstance(). Integer.toString(Calendar.DAY_OF_MONTH) + "/" + Integer.toString(Calendar.MONTH) + "/" + Integer.toString(Calendar.YEAR);
            String fec = getDateString();
            List<String> items = Arrays.asList(msg.split("#"));
            String[] r = msg.split("#");
            ContentValues row = new ContentValues();
            row.put("tipo", r[0]);
            row.put("fecha", fec);
            row.put("descripcion",r[1]);
            row.put("informacion", r[2]);

            db.insertSQL("Notificaciones", null, row);

        } catch (Exception e) {
            e.getMessage();
        }finally {
            db.close();
        }
    }
}
