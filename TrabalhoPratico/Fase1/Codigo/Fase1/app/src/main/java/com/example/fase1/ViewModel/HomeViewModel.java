package com.example.fase1.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullApp;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.User;
import com.example.fase1.Repositories.AppRepository;
import com.example.fase1.Repositories.PointOfInterestRepository;
import com.example.fase1.Repositories.TrailRepository;
import com.example.fase1.Repositories.UserRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private AppRepository appRepository;
    private TrailRepository trailRepository;
    private PointOfInterestRepository pointsRepository;
    private UserRepository userRepository;

    private LiveData<List<FullApp>> fullApp;
    private LiveData<List<FullPointOfInterest>> allPoints;
    private LiveData<List<FullTrail>> allTrails;
    private LiveData<List<User>> allUsers;


    public HomeViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        trailRepository = new TrailRepository(application);
        pointsRepository = new PointOfInterestRepository(application);
        userRepository = new UserRepository(application);

        fullApp = appRepository.getApp();
        allTrails = trailRepository.getAllTrails();
        allPoints = pointsRepository.getAllPoints();
        allUsers = userRepository.getUsers();
    }

    public LiveData<List<FullPointOfInterest>> getPoints(){
        return allPoints;
    }

    public LiveData<List<FullTrail>> getTrails(){
        return allTrails;
    }

    public LiveData<List<FullApp>> getFullApp(){
        return fullApp;
    }

    public LiveData<List<User>> getAllUsers () {
        return this.allUsers;
    }

}
