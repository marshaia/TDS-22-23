package com.example.fase1;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fase1.Adapters.PointsTrailAdapter;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.Trail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.Services.NotificationService;
import com.example.fase1.Tasks.CreatePathTask;
import com.example.fase1.ViewModel.TrailViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleTrailActivity extends AppCompatActivity {

    private List<LatLng> coordinatesPoints;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private String distanceTotal;
    private String secondsElapsed;
    private Boolean stateFav;
    private int visited;
    private int toVisit;


    @BindView((R.id.difficultyValue))
    TextView difficultyValue;
    @BindView((R.id.itineraryName))
    TextView itineraryName;
    @BindView((R.id.descriptionValue))
    TextView descriptionValue;
    @BindView((R.id.durationValue))
    TextView durationValue;
    @BindView((R.id.StartBtn))
    Button startButton;
    @BindView((R.id.StopBtn))
    Button stopButton;
    @BindView((R.id.starIcon))
    ImageView starIcon;


    private List<FullPointOfInterest> points;

    private PointsTrailAdapter adapter;
    private Intent serviceIntent;

    private TrailViewModel tvm;
    private int trail_id;
    private boolean isFavorite;

    public static final String CHANNEL_ID = "serviceChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trail);
        ButterKnife.bind(this);

        difficultyValue = findViewById(R.id.difficultyValue);
        itineraryName = findViewById(R.id.itineraryName);
        descriptionValue = findViewById(R.id.descriptionValue);
        durationValue = findViewById(R.id.durationValue);
        startButton = findViewById(R.id.StartBtn);
        stopButton = findViewById(R.id.StopBtn);
        starIcon = findViewById(R.id.starIcon);

        Intent intent = getIntent();
        trail_id = intent.getIntExtra("id",0);
        getPerms();
        LocalBroadcastManager.getInstance(this).registerReceiver(coordsReceiver, new IntentFilter("coordsSend"));

        RecyclerView view = findViewById(R.id.ListPoints);
        adapter = new PointsTrailAdapter(points,this,getApplication(),true);
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(adapter);

        tvm = new ViewModelProvider(this).get(TrailViewModel.class);
        tvm.setup(SingleTrailActivity.this,trail_id);
        LiveData<FullTrail> trailLive = tvm.getTrail();
        trailLive.observe(this, fullTrail -> {
            if(fullTrail!=null) setInfo(fullTrail.getTrail());
        });

        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);


        LiveData<List<FullPointOfInterest>> livePoint = tvm.getTrailPoints(trail_id);
        livePoint.observe(this, fullPointOfInterests -> {
            if(fullPointOfInterests!=null){
                points = fullPointOfInterests;
                coordinatesPoints = listPoints();

                adapter.setPointsList(points);
                view.setAdapter(adapter);

                mMapView.getMapAsync(callback);
            }
        });
        setupButtons();
    }

    public void startService(){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("points", (Serializable) points);
        serviceIntent.putExtra("id",trail_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ComponentName componentName = this.startForegroundService(serviceIntent);
            if (componentName != null) {
                String serviceName = componentName.getClassName();
                Log.d("MyApp", "Foreground service started: " + serviceName);
            } else {
                Log.e("MyApp", "Failed to start foreground service");
            }
        }

    }

    public void setupButtons(){

        stopButton.setEnabled(false);

        LiveData<List<Integer>> startedList = tvm.getTrailsStarted();
        startedList.observe(this, list -> {

            if (list.size()==1 && list.get(0).equals(trail_id)){
                stopButton.setEnabled(true);
                startButton.setEnabled(false);
            }

            startButton.setOnClickListener(view -> {
                if (list.size()==0 ){
                    tvm.addTrailToHistory();
                    stopButton.setEnabled(true);
                    startService();
                }
                else if (list.size()==1 && !list.get(0).equals(trail_id)){
                    Toast.makeText(getApplication(),"Já há um roteiro a decorrer", LENGTH_SHORT).show();
                }
            });

            stopButton.setOnClickListener(this::onBtnClickStop);

        });

    }


    private void showDialog(){ // TODO TOLDY strings nas strings
        AlertDialog.Builder builder  = new AlertDialog.Builder(SingleTrailActivity.this);
        View view = LayoutInflater.from(SingleTrailActivity.this).inflate(
                R.layout.layout_dialog_stop, findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textMessage)).setText(getString(R.string.Single_Trail_Total_Dist, distanceTotal));
        ((TextView) view.findViewById(R.id.textSeconds)).setText(getString(R.string.Single_Trail_Total_Seconds, secondsElapsed));
        ((TextView) view.findViewById(R.id.textVisited)).setText(getString(R.string.Single_Trail_dialog_Places_Visited, visited,toVisit));
        if(visited == toVisit){
            ((TextView) view.findViewById(R.id.textTitle)).setText(R.string.Single_Trail_dialog_title_concluded);

        }
        else{
            ((TextView) view.findViewById(R.id.textTitle)).setText(R.string.Single_Trail_dialog_title_stopped);

        }

        ((Button) view.findViewById(R.id.ButtonDialog)).setText(R.string.Single_Trail_dialog_ok);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.ButtonDialog).setOnClickListener(view1 -> {
            alertDialog.dismiss();
            Toast.makeText(SingleTrailActivity.this,"SUCCESS", LENGTH_SHORT).show();
        });

        alertDialog.show();
        startButton.setEnabled(true);

    }

    public void changeStar(View view){

        if (stateFav){
            starIcon.setImageResource(R.drawable.star_empty);
            stateFav = false;
        }
        else{
            starIcon.setImageResource(R.drawable.star);
            stateFav = true;
        }
        tvm.setFavorite(stateFav);
    }


    public List<LatLng> listPoints(){
        List<LatLng> coordinates = new ArrayList<>();
        for (FullPointOfInterest f : points){
            LatLng coordinate = new LatLng(f.getPoint().getPin_lat(),f.getPoint().getPin_lng());
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    public void setInfo(Trail trail){

        LiveData<List<TrailFavorite>> favorites = tvm.getFavorites();
        favorites.observe(this, trailFavorites -> {
            isFavorite = trailFavorites.size() > 0;

            itineraryName.setText(trail.getTrail_name());
            durationValue.setText(String.valueOf(trail.getTrail_duration()));
            descriptionValue.setText(trail.getTrail_desc());
            difficultyValue.setText(trail.getTrail_difficulty());

            if (isFavorite){
                starIcon.setImageResource(R.drawable.star);
                stateFav = true;
            }
            else{
                starIcon.setImageResource(R.drawable.star_empty);
                stateFav = false;
            }

            ImageView thumbnail = findViewById(R.id.imageViewFree);
            String link = trail.getTrail_img().replace("http", "https");
            Picasso.get().load(link).into(thumbnail);
        });



    }


    OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap){


            String[] waypoints = tvm.markersWaypoints(googleMap,coordinatesPoints,points);


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinatesPoints.get(0), 13));

            //createPath(waypoints,googleMap);
            String origin = coordinatesPoints.get(0).latitude + "," + coordinatesPoints.get(0).longitude;
            String destination = coordinatesPoints.get(coordinatesPoints.size()-1).latitude + "," + coordinatesPoints.get(coordinatesPoints.size()-1).longitude;
            new CreatePathTask(getApplication(),googleMap,origin,destination).execute(waypoints);

        }
    };


    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }




    public void goToGoogleMap(String latitude, String longitude) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.google.com/maps/dir/?api=1&origin=").append(latitude).append(",").append(longitude).append("&waypoints=");

        String pointName;
        for(int i = 0; i<points.size();i++) {
            if(i==points.size()-1){
                pointName = points.get(i).getPoint().getPin_name();
                String[] words = pointName.split(" ");
                urlBuilder.append("&destination=");
                for (int j =0 ;j<words.length;j++){
                    if(j!= words.length-1){
                        urlBuilder.append(words[j]).append("+");
                    }
                    else{
                        urlBuilder.append(words[j]);
                    }
                }
                urlBuilder.append("&travelmode=driving");
            }
            else if(points.size() >= 3 && i==coordinatesPoints.size()-2){
                pointName = points.get(i).getPoint().getPin_name();
                String[] words = pointName.split(" ");
                for (int j =0 ;j<words.length;j++){
                    if(j!= words.length-1){
                        urlBuilder.append(words[j]).append("+");
                    }
                    else{
                        urlBuilder.append(words[j]);
                    }
                }
            }
            else{
                pointName = points.get(i).getPoint().getPin_name();
                String[] words = pointName.split(" ");
                for (int j =0 ;j<words.length;j++){
                    if(j!= words.length-1){
                        urlBuilder.append(words[j]).append("+");
                    }
                    else{
                        urlBuilder.append(words[j]).append("|");
                    }
                }
            }
        }
        String address = urlBuilder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onBtnClickStop(View view) {
        if (serviceIntent != null){
            serviceIntent.putExtra("Stop", true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
        }
        else{
            serviceIntent = new Intent(this, NotificationService.class);
            serviceIntent.putExtra("Stop", true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
        }
    }


    private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            distanceTotal = intent.getStringExtra("distance");
            secondsElapsed = intent.getStringExtra("timeElapsed");
            toVisit = intent.getIntExtra("toVisit",0);
            visited = intent.getIntExtra("visited",0);
            if(visited > toVisit/2){
                tvm.finishTrail(distanceTotal,secondsElapsed);   // para adicionar caso tenha passado por mais de metade dos pontos + chegou ao ultimo
            }
            else{
                tvm.cancelTrail(distanceTotal,secondsElapsed);
            }
            showDialog();
            stopButton.setEnabled(false);

            cleanup();
        }
    };


    public void cleanup(){
        SharedPreferences sp2 = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp2.edit();
        editor.remove("last_lng");
        editor.remove("last_lat");
        editor.remove("points");
        editor.remove("trailDistance");
        editor.remove("trail_id");
        editor.remove("trailTime");
        editor.apply();
        Log.e("CLEANUP", "CLEANED UP");
    }


    private final BroadcastReceiver coordsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String longitude = intent.getStringExtra("longitude");
            String latitude = intent.getStringExtra("latitude");
            goToGoogleMap(latitude,longitude);
        }
    };



    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReceiver, new IntentFilter("my-event"));
        mMapView.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dataReceiver);
        mMapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if(serviceIntent != null)stopService(serviceIntent);
        mMapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    /**
     *  REQUEST PERMISSIONS
     */

    @SuppressLint("MissingPermission")
    private void getPerms() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (!isLocationEnabled()) {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    // method to check for permissions
    /*private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&         ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
    }
     */

    private boolean checkPermissions() {
        boolean hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasFineLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean hasBackgroundLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            return hasCoarseLocationPermission && hasFineLocationPermission && hasBackgroundLocationPermission;
        } else {
            return hasCoarseLocationPermission && hasFineLocationPermission;
        }
    }



    // method to request for permissions
    private void requestPermissions() {
        int PERMISSION_ID = 44;
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}