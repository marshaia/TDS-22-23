package com.example.fase1;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Adapters.PointHistoryAdapter;
import com.example.fase1.Adapters.TrailHistoryAdapter;
import com.example.fase1.Data.PointHistory;
import com.example.fase1.Data.TrailHistory;
import com.example.fase1.ViewModel.HistoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<TrailHistory> trailsHistory;
    private ArrayList<PointHistory> pointsHistory;

    private TrailHistoryAdapter adapterTrails;
    private PointHistoryAdapter adapterPoints;

    @BindView((R.id.recyclerViewHistory))
    RecyclerView recyclerView;

    @BindView((R.id.buttonTrails))
    Button buttonTrails;

    @BindView((R.id.buttonPoints))
    Button buttonPoints;

    private String filter = "trails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);
        setTitle("History");

        trailsHistory = new ArrayList<>();
        pointsHistory = new ArrayList<>();

        HistoryViewModel vm = new ViewModelProvider(this).get(HistoryViewModel.class);

        adapterTrails = new TrailHistoryAdapter(trailsHistory,this,getApplication());
        adapterPoints = new PointHistoryAdapter(pointsHistory,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapterTrails);

        LiveData<List<TrailHistory>> trailHistory = vm.getTrailHistory();
        LiveData<List<PointHistory>> pointHistory = vm.getPointHistory();


        //--------------------------------------- Observar Trails History e Points History-------------------------------

        trailHistory.observe(this, trailslist -> {
            ArrayList<TrailHistory> trailsArrayList = new ArrayList<>(trailslist);
            adapterTrails.setTrailsHistory(trailsArrayList);
            if(Objects.equals(filter, "trails"))
                recyclerView.setAdapter(adapterTrails);
        });

        pointHistory.observe(this, pointsList -> {
            ArrayList<PointHistory> pointsArrayList = new ArrayList<>(pointsList);
            adapterPoints.setPointsHistory(pointsArrayList);
            if(Objects.equals(filter, "points"))
                recyclerView.setAdapter(adapterPoints);
        });

        setupButtons();
    }

    public void setupButtons(){

        buttonPoints.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HistoryActivity.this, R.color.lighterLogoGreen)));

        buttonTrails.setOnClickListener(view -> {
            buttonTrails.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HistoryActivity.this, R.color.logoGreen)));
            buttonPoints.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HistoryActivity.this, R.color.lighterLogoGreen)));
            recyclerView.setAdapter(adapterTrails);
            filter = "trails";
        });

        buttonPoints.setOnClickListener(view -> {
            buttonPoints.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HistoryActivity.this, R.color.logoGreen)));
            buttonTrails.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(HistoryActivity.this, R.color.lighterLogoGreen)));
            recyclerView.setAdapter(adapterPoints);
            filter = "points";
        });
    }
}