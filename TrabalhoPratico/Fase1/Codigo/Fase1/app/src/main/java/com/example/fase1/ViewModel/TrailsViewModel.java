package com.example.fase1.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullTrail;
import com.example.fase1.Repositories.TrailRepository;

import java.util.List;

public class TrailsViewModel extends AndroidViewModel {

    private TrailRepository trailRepository;

    private final LiveData<List<FullTrail>> allTrails;

    public TrailsViewModel(Application application) {
        super(application);
        trailRepository = new TrailRepository(application);
        allTrails = trailRepository.getAllTrails();
    }

    public LiveData<List<FullTrail>> getAllTrails() {
        return allTrails;
    }

}