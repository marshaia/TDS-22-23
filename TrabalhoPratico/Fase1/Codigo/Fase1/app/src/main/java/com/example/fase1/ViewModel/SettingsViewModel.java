package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SettingsViewModel extends AndroidViewModel {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveSettings(String selectedValueDistance, String selectedValueTime, Boolean state){
        editor.putString("locationInterval",selectedValueTime);
        editor.putString("locationDistance",selectedValueDistance);
        editor.putString("notifsSwitch", String.valueOf(state));
        editor.apply();
    }


    public Boolean getNotifSwitch(){
        String selectedValueString= sp.getString("notifsSwitch",null);
        Boolean state;
        if (selectedValueString == null) state = true;
        else{
            state = Boolean.parseBoolean(selectedValueString);
        }
        return state;
    }



    public String getLocationDistance(){
        String tempDistance =  sp.getString("locationDistance",null);
        String selectedValueDistance;
        if (tempDistance == null) selectedValueDistance = "100";
        else{
            selectedValueDistance = tempDistance;

        }
        return selectedValueDistance;
    }

    public String getLocationInterval(){
        String tempInterval = sp.getString("locationInterval", null);
        String result;
        if (tempInterval == null) result = "10";
        else{
            result = tempInterval;
        }
        return result;
    }



}
