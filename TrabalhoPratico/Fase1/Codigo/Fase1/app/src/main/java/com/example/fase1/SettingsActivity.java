package com.example.fase1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fase1.ViewModel.SettingsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    private String selectedValueTime;
    private String selectedValueDistance;
    SettingsViewModel svm;

    private boolean state;

    @BindView((R.id.spinner))
    Spinner spinnerTimer;
    @BindView((R.id.spinner3))
    Spinner spinnerDistance;
    @BindView((R.id.switchMaterial))
    SwitchCompat notifsSwitch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setTitle("Settings");

        String[] timeOptions = {"10","15","30","60"};
        String[] distanceOptions = {"100","150","250","300"};

        svm = new ViewModelProvider(this).get(SettingsViewModel.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerTimer.setAdapter(adapter);
        spinnerTimer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValueTime = parent.getItemAtPosition(position).toString();
                svm.saveSettings(selectedValueDistance,selectedValueTime,state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> adapterDistance = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceOptions);
        adapterDistance.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerDistance.setAdapter(adapterDistance);
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValueDistance = parent.getItemAtPosition(position).toString();
                svm.saveSettings(selectedValueDistance,selectedValueTime,state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        getSavedValues();

        spinnerDistance.setSelection(adapterDistance.getPosition(selectedValueDistance));
        spinnerTimer.setSelection(adapter.getPosition(selectedValueTime));

        notifsSwitch.setChecked(state);

        notifsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            state = isChecked;
            svm.saveSettings(selectedValueDistance,selectedValueTime,state);
        });

    }

    public void getSavedValues(){
        state =svm.getNotifSwitch();
        selectedValueDistance = svm.getLocationDistance();
        selectedValueTime = svm.getLocationInterval();
    }


}