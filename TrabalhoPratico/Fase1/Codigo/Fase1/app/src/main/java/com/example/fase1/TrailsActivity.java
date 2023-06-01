package com.example.fase1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Adapters.TrailsAdapter;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.ViewModel.TrailsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailsActivity extends BaseActivity {

    private TrailsAdapter adapter;

    @BindView((R.id.recyclerViewTrails))
    RecyclerView view;

    @Override
    protected int getLayoutResourceId() { return R.layout.activity_trails;}
    @Override
    protected int getNavBarItemSelected () {return R.id.navBar_roteiros;}

    @Override
    protected void afterNavBar() {
        ArrayList<FullTrail> trails = new ArrayList<>();

        setTitle("Trails");
        ButterKnife.bind(this);

        adapter = new TrailsAdapter(trails,this,getApplication());
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        view.setAdapter(adapter);

        TrailsViewModel tvm = new ViewModelProvider(this).get(TrailsViewModel.class);

        LiveData<List<FullTrail>> trailsData = tvm.getAllTrails();
        trailsData.observe(this, trailslist -> {
            ArrayList<FullTrail> trailsArrayList = new ArrayList<>(trailslist);
            adapter.setTrails(trailsArrayList);
            view.setAdapter(adapter);
        });

    }

}