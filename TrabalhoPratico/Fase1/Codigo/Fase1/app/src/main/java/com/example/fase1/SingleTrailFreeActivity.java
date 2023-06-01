package com.example.fase1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.fase1.Adapters.PointsTrailAdapter;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.Trail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.ViewModel.TrailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleTrailFreeActivity extends AppCompatActivity {

    private List<FullPointOfInterest> points;

    private PointsTrailAdapter adapter;

    private TrailViewModel tvm;

    @BindView((R.id.difficultyValue))
    TextView difficultyValue;
    @BindView((R.id.itineraryName))
    TextView itineraryName;
    @BindView((R.id.descriptionValue))
    TextView descriptionValue;
    @BindView((R.id.durationValue))
    TextView durationValue;
    @BindView((R.id.starIcon))
    ImageView starIcon;




    private Boolean stateFav;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trail_free);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        int trail_id = intent.getIntExtra("id", 0);


        RecyclerView view = findViewById(R.id.ListPoints);
        adapter = new PointsTrailAdapter(points,this,getApplication(),false);
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(adapter);

        tvm = new ViewModelProvider(this).get(TrailViewModel.class);
        tvm.setup(SingleTrailFreeActivity.this, trail_id);
        LiveData<FullTrail> trailLive = tvm.getTrail();
        trailLive.observe(this, fullTrail -> {
            if(fullTrail!=null)
                setInfo(fullTrail.getTrail());
        });


        LiveData<List<FullPointOfInterest>> livePoint = tvm.getTrailPoints(trail_id);
        livePoint.observe(this, pointsOfInterest -> {
            points = pointsOfInterest;

            adapter.setPointsList(points);
            view.setAdapter(adapter);
        });


    }


    public void changeStar(View view){

        if (stateFav){
            starIcon.setImageResource(R.drawable.star_empty);
            stateFav = false;
        }
        else{
            starIcon.setImageResource(R.drawable.star);
            stateFav = true;
        }
        tvm.setFavorite(stateFav);
    }

    public void setInfo(Trail trail){

        LiveData<List<TrailFavorite>> favorites = tvm.getFavorites();
        favorites.observe(this, trailFavorites -> {
            stateFav = trailFavorites.size() > 0;

            itineraryName.setText(trail.getTrail_name());
            durationValue.setText(String.valueOf(trail.getTrail_duration()));
            descriptionValue.setText(trail.getTrail_desc());
            difficultyValue.setText(trail.getTrail_difficulty());
            ImageView thumbnail = findViewById(R.id.imageViewFree);

            if (stateFav){
                starIcon.setImageResource(R.drawable.star);
                stateFav = true;
            }
            else{
                starIcon.setImageResource(R.drawable.star_empty);
                stateFav = false;
            }

            String link = trail.getTrail_img().replace("http", "https");
            Picasso.get().load(link).into(thumbnail);
        });

    }


    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}