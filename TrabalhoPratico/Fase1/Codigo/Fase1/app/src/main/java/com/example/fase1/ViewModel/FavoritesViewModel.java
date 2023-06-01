package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.Repositories.TrailFavoriteRepository;
import com.example.fase1.Repositories.TrailRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel{

    private TrailFavoriteRepository favoriteRepository;
    private TrailRepository trailsRepository;

    private LiveData<List<TrailFavorite>> favoriteTrails;
    private LiveData<List<FullTrail>> allTrails;


    public FavoritesViewModel(Application application) {
        super(application);

        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        favoriteRepository = new TrailFavoriteRepository(application,isPremium);
        trailsRepository = new TrailRepository(application);
        favoriteTrails = favoriteRepository.getAllFavorites();
        allTrails = trailsRepository.getAllTrails();
    }

    public LiveData<List<TrailFavorite>> getFavoriteTrails() {
        return favoriteTrails;
    }

    public LiveData<List<FullTrail>> getTrails() {
        return allTrails;
    }

    public LiveData<FullTrail> getTrail(int id){
        return trailsRepository.getTrailById(id);
    }



}