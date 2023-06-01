package com.example.fase1.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.Trail;
import com.example.fase1.Data.TrailFavorite;
import com.example.fase1.R;
import com.example.fase1.SingleTrailActivity;
import com.example.fase1.SingleTrailFreeActivity;
import com.example.fase1.ViewModel.FavoritesViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private ArrayList<TrailFavorite> favorites;
    private final Activity activity;
    private final Application application;

    public FavoritesAdapter(ArrayList<TrailFavorite> favorites, Activity activity, Application application) {
        this.favorites = favorites;
        this.activity = activity;
        this.application = application;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoritecard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int trail_id = favorites.get(position).getTrail_id();

        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        FavoritesViewModel fvm = new ViewModelProvider((ViewModelStoreOwner) activity).get(FavoritesViewModel.class);
        LiveData<List<FullTrail>> trails = fvm.getTrails();
        trails.observe((LifecycleOwner) this.activity, new Observer<List<FullTrail>>() {
            @Override
            public void onChanged(List<FullTrail> fullTrails) {
            }
        });

        LiveData<FullTrail> trail = fvm.getTrail(trail_id);
        trail.observe((LifecycleOwner) this.activity, new Observer<FullTrail>() {
            @Override
            public void onChanged(FullTrail fullTrail) {

                Trail trail = fullTrail.getTrail();

                String durationString = trail.getTrail_duration() + " minutos";

                holder.title.setText(trail.getTrail_name());
                holder.duration.setText(durationString);
                holder.image.setImageBitmap(getBitMap(trail.getTrail_img()));
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent;

                        if (isPremium) intent = new Intent(activity, SingleTrailActivity.class);
                        else intent = new Intent(activity, SingleTrailFreeActivity.class);

                        intent.putExtra("id", trail.getId());
                        activity.startActivity(intent);
                    }
                });


            }
        });




    }


    @Override
    public int getItemCount() {
        return favorites.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView((R.id.favoritetitle))
        TextView title;
        @BindView((R.id.favoriteimage))
        ImageView image;
        @BindView((R.id.favoriteduration))
        TextView duration;
        @BindView((R.id.favoriteCard))
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setTrails(ArrayList<TrailFavorite> favorites) {
        this.favorites = favorites;
    }

    public Bitmap getBitMap(String imageUrl) {

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        File file = new File(application.getFilesDir(), filename);
        Bitmap image = null;

        if (file.exists())
            image = BitmapFactory.decodeFile(file.getAbsolutePath());

        return image;
    }

}