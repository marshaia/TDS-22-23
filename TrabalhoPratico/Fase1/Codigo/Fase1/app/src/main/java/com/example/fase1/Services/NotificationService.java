package com.example.fase1.Services;

import static com.example.fase1.SingleTrailActivity.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.text.HtmlCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.PointOfInterestActivity;
import com.example.fase1.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class NotificationService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private List<FullPointOfInterest> points;
    private HashMap<Integer, Boolean> states;
    private Integer selectedValueTime;
    private Integer selectedValueDistance;
    private Location aux;

    private float distanceTotal;
    private float distanceTotalAux;
    private float secondsElapsed;
    private float secondsElapsedAux;
    private int stateSize;
    private int visited;
    private Boolean sent;
    private String selectedTimeString;
    private String notifsSwitch;
    private String selectedDistanceString;
    private int notifIndex;
    private boolean isStoppedByCallingComponent = false;
    private int trail_id;

    private SharedPreferences sp2;
    public void initStates() {
        for (FullPointOfInterest p : points) {
            states.put(p.getPoint().getId(), false);
        }
        stateSize = states.size();

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int visited = 0;
        sp2 = getSharedPreferences("shared preferences",MODE_PRIVATE);

        states = new HashMap<>();
        sent = false;

        points = (List<FullPointOfInterest>) intent.getSerializableExtra("points");
        distanceTotalAux = intent.getFloatExtra("trailDistance",0);
        secondsElapsedAux = intent.getFloatExtra("trailTime",0);

        float lat = intent.getFloatExtra("last_lat",0);
        float lng = intent.getFloatExtra("last_lng",0);

        if(lat!=0 && lng!=0){
            aux = new Location("Location");
            aux.setLatitude(lat);
            aux.setLongitude(lng);
        }

        if(points !=null){
            initStates();

        }

        notifIndex = 0;


        isStoppedByCallingComponent = false;
        boolean stop = intent.getBooleanExtra("Stop",false);
        trail_id = intent.getIntExtra("id",1);


        getSavedValues();


        if(!stop){
            distanceTotal = distanceTotalAux;
            secondsElapsed = secondsElapsedAux;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (points != null) checkUserDistanceToPoints(location);
                    if (!sent) {

                        sendCoordsToActivity(location);
                        sent = true;
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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, selectedValueTime, 0,locationListener);


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Location Service")
                    .setContentText("Location tracking is enabled")
                    .build();

            startForeground(1, notification);

        }
        else{
            stopForeground(true);
            isStoppedByCallingComponent = true;
            sendDataToActivity();
            stopSelf();
        }


        return START_NOT_STICKY;
    }


    public void makeNotif(int id, String name, Integer photoID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Notification Pin", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Spanned mini = HtmlCompat.fromHtml(getString(R.string.MiniNotif), HtmlCompat.FROM_HTML_MODE_LEGACY);
        Spanned big = HtmlCompat.fromHtml(getString(R.string.BigNotif), HtmlCompat.FROM_HTML_MODE_LEGACY);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(photoID)
                .setContentTitle(name)
                .setContentText(mini)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(big))
                .setAutoCancel(true);

        Intent intent = new Intent(this, PointOfInterestActivity.class);
        intent.putExtra("pointID",id);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notifIndex, builder.build());
    }

    public void getSavedValues(){
        selectedTimeString = sp2.getString("locationInterval",null);
        notifsSwitch = sp2.getString("notifsSwitch",null);
        selectedDistanceString = sp2.getString("locationDistance",null);

        if(selectedTimeString == null) selectedValueTime = 10000;
        else {
            selectedValueTime = Integer.parseInt(selectedTimeString) * 1000;
        }
        if(notifsSwitch == null) {
            notifsSwitch = "true";
        }
        if(selectedDistanceString ==null) selectedValueDistance = 100;
        else selectedValueDistance = Integer.parseInt(selectedDistanceString);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isStoppedByCallingComponent){
            Log.e("ONDESTROY: ", "STOPPED");
        }
        if (locationManager != null) locationManager.removeUpdates(locationListener);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendDataToActivity() {
        Intent intent = new Intent("my-event");
        intent.putExtra("distance", Float.toString(distanceTotal));
        intent.putExtra("timeElapsed",Float.toString(secondsElapsed));
        intent.putExtra("toVisit",stateSize);
        intent.putExtra("visited",visited);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void sendCoordsToActivity(Location location) {
        Intent intent = new Intent("coordsSend");
        intent.putExtra("latitude", String.valueOf(location.getLatitude()));
        intent.putExtra("longitude",String.valueOf(location.getLongitude()));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void saveState(){
        SharedPreferences.Editor editor = sp2.edit();
        editor.putFloat("trailDistance",distanceTotal);
        editor.putFloat("trailTime",secondsElapsed);
        editor.putFloat("last_lat", (float) aux.getLatitude());
        editor.putFloat("last_lng", (float) aux.getLongitude());
        editor.putInt("trail_id",trail_id);
        editor.putString("running", "true");

        saveMyObjects(editor);
    }


    public void saveMyObjects(SharedPreferences.Editor editor) {

        Gson gson = new Gson();
        String myObjectsJson = gson.toJson(points);
        editor.putString("points", myObjectsJson);
        editor.apply();
    }


    public void checkUserDistanceToPoints (Location location) {
        secondsElapsed = secondsElapsed + selectedValueTime / 1000;
        if(aux == null){
            aux = location;
        }
        else{
            distanceTotal += location.distanceTo(aux) ;
            aux = location;
        }
        saveState();
        getSavedValues();

        notifsSwitch = sp2.getString("notifsSwitch",null);
        if(notifsSwitch == null) notifsSwitch = "true";

        if(notifsSwitch.equals("true")){
            for (FullPointOfInterest p : points){
                if (Boolean.FALSE.equals(states.get(p.getPoint().getId()))){
                    Location targetLocation = new Location("");
                    targetLocation.setLatitude(p.getPoint().getPin_lat());
                    targetLocation.setLongitude(p.getPoint().getPin_lng());
                    float distance = location.distanceTo(targetLocation);
                    if (distance < selectedValueDistance){
                        visited++;

                        makeNotif(p.getPoint().getId(), p.getPoint().getPin_name(), R.drawable.location);
                        states.put(p.getPoint().getId(),true);
                        notifIndex++;
                    }
                }
            }
        }
    }
}
