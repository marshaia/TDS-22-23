package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fase1.Data.Edge;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.InfoTrail;
import com.example.fase1.Data.Trail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.Data.TrailHistory;
import com.example.fase1.Repositories.TrailFavoriteRepository;
import com.example.fase1.Repositories.TrailHistoryRepository;
import com.example.fase1.Repositories.TrailRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TrailViewModel extends AndroidViewModel {

    private WeakReference<Activity> activityRef;
    private TrailRepository trailRepository;
    private TrailHistoryRepository trailHistoryRepository;
    private TrailFavoriteRepository trailFavoriteRepository;
    private LiveData<FullTrail> trail;
    private boolean isPremium;
    private Application app;



    public TrailViewModel(Application application) {
        super(application);
        app = application;
        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        isPremium = sp.getBoolean("usertype",false);

        trailRepository = new TrailRepository(application);
        trailHistoryRepository = new TrailHistoryRepository(application,isPremium);
        trailFavoriteRepository = new TrailFavoriteRepository(application,isPremium);

    }


    public void setup(Activity activity, int id){
        this.activityRef = new WeakReference<>(activity);
        trail = trailRepository.getTrailById(id);
    }

    public LiveData<FullTrail> getTrail(){
        return trail;
    }

    public LiveData<List<FullPointOfInterest>> getTrailPoints(int id){
        return trailRepository.getTrailPoints(id);
    }

    public void addTrailToHistory(){

        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        Date date = new Date(System.currentTimeMillis());
        TrailHistory th = new TrailHistory(trail.getValue().getTrail().getId(),isPremium,false,date);
        trailHistoryRepository.insert(th);
        Trail trailValue = trail.getValue().getTrail();
        List<Edge> edgesValue = trail.getValue().getEdges();
        List<InfoTrail> infosValue = trail.getValue().getInfos();

        trailValue.setStarted(true);
        trailRepository.insert(trailValue,edgesValue,infosValue);
    }

    public void cancelTrail(String distance, String time){
        Trail trailValue = trail.getValue().getTrail();
        List<Edge> edgesValue = trail.getValue().getEdges();
        List<InfoTrail> infosValue = trail.getValue().getInfos();

        Date current = new Date(System.currentTimeMillis());

        LiveData<TrailHistory> th = trailHistoryRepository.getLastTrailHistory(trailValue.getId());
        th.observe((LifecycleOwner) this.activityRef.get(), new Observer<TrailHistory>() {
            @Override
            public void onChanged(TrailHistory trailHistory) {
                trailHistory.setLast_update(current);
                trailHistory.setCancelled(true);
                trailHistory.setDistance(distance);
                trailHistory.setTime(time);
                trailHistory.setDate(current);
                trailHistoryRepository.insert(trailHistory);
            }
        });

        trailValue.setStopped(true);
        trailValue.setStarted(false);
        trailRepository.insert(trailValue,edgesValue,infosValue);
    }


    public void finishTrail(String distance, String time){

        Trail trailValue = trail.getValue().getTrail();
        List<Edge> edgesValue = trail.getValue().getEdges();
        List<InfoTrail> infosValue = trail.getValue().getInfos();

        Date current = new Date(System.currentTimeMillis());

        LiveData<TrailHistory> th = trailHistoryRepository.getLastTrailHistory(trailValue.getId());
        th.observe((LifecycleOwner) this.activityRef.get(), new Observer<TrailHistory>() {
            @Override
            public void onChanged(TrailHistory trailHistory) {
                trailHistory.setLast_update(current);
                trailHistory.setFinished(true);
                trailHistory.setDistance(distance);
                trailHistory.setTime(time);
                trailHistory.setDate(current);
                trailHistoryRepository.insert(trailHistory);
            }
        });

        trailValue.setStopped(true);
        trailValue.setStarted(false);
        trailRepository.insert(trailValue,edgesValue,infosValue);
    }

    public LiveData<List<Integer>> getTrailsStarted(){
        return trailRepository.getTrailsStarted();
    }

    public void setFavorite(Boolean favorite){

        int trail_id = trail.getValue().getTrail().getId();

        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        TrailFavorite fav = new TrailFavorite(trail_id,isPremium);

        if(favorite) trailFavoriteRepository.insert(fav);
        else trailFavoriteRepository.deleteFavorite(trail_id,isPremium);
    }

    public LiveData<List<TrailFavorite>> getFavorites(){
        int id = trail.getValue().getTrail().getId();
        return trailFavoriteRepository.getAllFavoritesFromTrail(id,isPremium);
    }

    public String[] markersWaypoints(GoogleMap googleMap, List<LatLng> coordinatesPoints, List<FullPointOfInterest> points){
            String[] waypoints = new String[coordinatesPoints.size() - 2];
        Arrays.fill(waypoints, ""); // Initializes array with empty string
        for (int i = 0; i < coordinatesPoints.size(); i++){
            googleMap.addMarker(new MarkerOptions().position(coordinatesPoints.get(i)).title(points.get(i).getPoint().getPin_name()));
            if(i != 0 && i != coordinatesPoints.size() - 1){
                waypoints[i - 1] = coordinatesPoints.get(i).latitude + "," + coordinatesPoints.get(i).longitude;
            }
        }
        return waypoints;
    }



}