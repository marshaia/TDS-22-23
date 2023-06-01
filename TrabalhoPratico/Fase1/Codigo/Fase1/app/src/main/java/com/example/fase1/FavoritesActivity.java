package com.example.fase1;

import android.os.Bundle;

import com.example.fase1.Adapters.FavoritesAdapter;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.ViewModel.FavoritesViewModel;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<TrailFavorite> favorites;
    private FavoritesAdapter adapter;

    @BindView((R.id.recyclerViewFavorites))
    RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        this.favorites = new ArrayList<>();

        setTitle("Favorites");
        ButterKnife.bind(this);

        adapter = new FavoritesAdapter(favorites,this,getApplication());
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(adapter);

        FavoritesViewModel fvm = new ViewModelProvider(this).get(FavoritesViewModel.class);

        LiveData<List<TrailFavorite>> trailsData = fvm.getFavoriteTrails();
        trailsData.observe(this, trailsFavoriteList -> {
            ArrayList<TrailFavorite> trailsArrayList = new ArrayList<>(trailsFavoriteList);
            adapter.setTrails(trailsArrayList);
            view.setAdapter(adapter);
        });

    }

}