package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.PointHistory;
import com.example.fase1.Repositories.PointHistoryRepository;
import com.example.fase1.Repositories.PointOfInterestRepository;

import java.util.Date;

public class PointViewModel extends AndroidViewModel {

    private PointOfInterestRepository pointRepository;
    private PointHistoryRepository pointHistoryRepository;
    private LiveData<FullPointOfInterest> point;

    public PointViewModel(Application application) {
        super(application);

        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        pointRepository = new PointOfInterestRepository(application);
        pointHistoryRepository = new PointHistoryRepository(application,isPremium);
    }

    public void setup(int id){
        point = pointRepository.getPointById(id);
    }

    public LiveData<FullPointOfInterest> getPoint() {
        return point;
    }

    public void addPointToHistory(int id){

        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        Date current = new Date(System.currentTimeMillis());
        PointHistory ph = new PointHistory(id,isPremium,current);
        pointHistoryRepository.insert(ph);
    }

    public Boolean getPremium () {
        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences",MODE_PRIVATE);
        return sp.getBoolean("usertype", false);
    }

}