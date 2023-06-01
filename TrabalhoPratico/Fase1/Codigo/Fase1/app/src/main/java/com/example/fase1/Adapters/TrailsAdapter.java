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
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.Trail;
import com.example.fase1.R;
import com.example.fase1.SingleTrailActivity;
import com.example.fase1.SingleTrailFreeActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailsAdapter extends RecyclerView.Adapter<TrailsAdapter.ViewHolder> {

    private ArrayList<FullTrail> trails;
    private final Activity activity;
    private final Application application;

    public TrailsAdapter(ArrayList<FullTrail> trails, Activity activity, Application application){
        this.trails = trails;
        this.activity = activity;
        this.application = application;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailcard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences sp = application.getSharedPreferences("shared preferences", MODE_PRIVATE);
        boolean isPremium = sp.getBoolean("usertype",false);

        Trail trail = trails.get(position).getTrail();

        String durationString = trail.getTrail_duration() + " minutos";

        holder.title.setText(trail.getTrail_name());
        holder.duration.setText(durationString);
        holder.image.setImageBitmap(getBitMap(trail.getTrail_img()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;

                if(isPremium) intent = new Intent(activity, SingleTrailActivity.class);
                else intent = new Intent(activity, SingleTrailFreeActivity.class);

                intent.putExtra("id",trail.getId());
                activity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return trails.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView((R.id.trailtitle))
        TextView title;
        @BindView((R.id.trailimage))
        ImageView image;
        @BindView((R.id.trailduration))
        TextView duration;
        @BindView((R.id.trailCard))
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setTrails(ArrayList<FullTrail> trails) {
        this.trails = trails;
    }

    public Bitmap getBitMap(String imageUrl){

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        File file = new File(application.getFilesDir(), filename);
        Bitmap image = null;

        if (file.exists())
            image = BitmapFactory.decodeFile(file.getAbsolutePath());

        return image;
    }

}
