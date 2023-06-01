package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.User;
import com.example.fase1.Repositories.UserRepository;

import java.util.List;


public class ProfileViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private Application app;

    private LiveData<List<User>> users;

    public ProfileViewModel(Application application) {
        super(application);
        app = application;
        userRepository = new UserRepository(application);
        users = userRepository.getUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void logout(){

        SharedPreferences sp3 = app.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp3.edit();
        editor.remove("cookies");
        editor.remove("usertype");
        editor.remove("locationInterval");
        editor.remove("locationDistance");
        editor.remove("notifsSwitch");
        editor.apply();

        userRepository.delete();
    }


    public Boolean getPremium () {
        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences",MODE_PRIVATE);
        return sp.getBoolean("usertype", false);
    }
}
