package com.example.fase1.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
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
import com.example.fase1.Data.TrailHistory;
import com.example.fase1.R;
import com.example.fase1.SingleTrailActivity;
import com.example.fase1.ViewModel.HistoryViewModel;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailHistoryAdapter extends RecyclerView.Adapter<TrailHistoryAdapter.ViewHolder>{


    private ArrayList<TrailHistory> trailsHistory;
    private final Activity activity;
    private final Application application;

    public TrailHistoryAdapter(ArrayList<TrailHistory> trailsHistory, Activity activity, Application application) {
        this.trailsHistory = trailsHistory;
        this.activity = activity;
        this.application = application;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailhistorycard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TrailHistory th = trailsHistory.get(position);

        HistoryViewModel hvm = new ViewModelProvider((ViewModelStoreOwner) activity).get(HistoryViewModel.class);
        LiveData<FullTrail> liveTrail = hvm.getTrailById(th.getTrail_id());

        liveTrail.observe((LifecycleOwner) this.activity, new Observer<FullTrail>() {
            @Override
            public void onChanged(FullTrail fullTrail) {

                if(fullTrail != null) {
                    Trail trail = fullTrail.getTrail();

                    String dateString = "";

                    String durationString = trail.getTrail_duration() + " minutos";
                    Date date = th.getDate();

                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()); // get a localized date format based on the user's locale

                    if(date != null) dateString = formatter.format(date);
                    else dateString = "----";

                    holder.title.setText(trail.getTrail_name());
                    holder.duration.setText(durationString);
                    holder.distance.setText(th.getDistance());
                    holder.timeToFinish.setText(th.getTime());
                    holder.date.setText(dateString);
                    holder.image.setImageBitmap(getBitMap(trail.getTrail_img()));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, SingleTrailActivity.class);
                            intent.putExtra("id",th.getTrail_id());
                            activity.startActivity(intent);
                        }
                    });

                    int positionSaved = holder.getAdapterPosition();

                    boolean done = trailsHistory.get(positionSaved).getFinished();
                    boolean stopped = trailsHistory.get(positionSaved).isCancelled();

                    if(done) holder.doneImage.setVisibility(View.VISIBLE);
                    else if (stopped) holder.stoppedImage.setVisibility(View.VISIBLE);
                        else holder.notDoneImage.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailsHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView((R.id.trailtitle))
        TextView title;
        @BindView((R.id.trailduration))
        TextView duration;
        @BindView((R.id.trailDate))
        TextView distance;
        @BindView((R.id.trailDistanceTraveled))
        TextView timeToFinish;
        @BindView((R.id.trailTimeToFinish))
        TextView date;
        @BindView((R.id.trailimage ))
        ImageView image;
        @BindView((R.id.trailDone))
        ImageView doneImage;
        @BindView((R.id.trailNotDone))
        ImageView notDoneImage;
        @BindView((R.id.trailStopped))
        ImageView stoppedImage;
        @BindView((R.id.trailHistoryCard))
        RelativeLayout layout;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setTrailsHistory(ArrayList<TrailHistory> trailsHistory) {
        this.trailsHistory = trailsHistory;
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
