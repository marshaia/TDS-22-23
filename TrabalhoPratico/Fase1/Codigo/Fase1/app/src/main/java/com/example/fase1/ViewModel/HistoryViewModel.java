package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.PointHistory;
import com.example.fase1.Data.TrailHistory;
import com.example.fase1.Repositories.PointHistoryRepository;
import com.example.fase1.Repositories.PointOfInterestRepository;
import com.example.fase1.Repositories.TrailHistoryRepository;
import com.example.fase1.Repositories.TrailRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {


    private TrailHistoryRepository trailHistoryRepository;
    private PointHistoryRepository pointHistoryRepository;
    private PointOfInterestRepository pointRepository;
    private TrailRepository trailRepository;


    private final LiveData<List<TrailHistory>> allTrailHistory;
    private final LiveData<List<PointHistory>> allPointHistory;


    public HistoryViewModel(Application application) {
        super(application);

        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        trailHistoryRepository = new TrailHistoryRepository(application,isPremium);
        pointHistoryRepository = new PointHistoryRepository(application,isPremium);
        pointRepository = new PointOfInterestRepository(application);
        trailRepository = new TrailRepository(application);

        allTrailHistory = trailHistoryRepository.getAllHistory();
        allPointHistory = pointHistoryRepository.getAllHistory();
    }

    public LiveData<List<TrailHistory>> getTrailHistory() {
        return allTrailHistory;
    }

    public LiveData<List<PointHistory>> getPointHistory() {
        return allPointHistory;
    }

    public LiveData<FullPointOfInterest> getPointById(int id){
        return pointRepository.getPointById(id);
    }

    public LiveData<FullTrail> getTrailById(int id){
        return trailRepository.getTrailById(id);
    }

}