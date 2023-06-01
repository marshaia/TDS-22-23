package com.example.fase1.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.PointHistory;
import com.example.fase1.Data.PointOfInterest;
import com.example.fase1.PointOfInterestActivity;
import com.example.fase1.R;
import com.example.fase1.ViewModel.HistoryViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.ViewHolder>{


    private ArrayList<PointHistory> pointsHistory;
    private final Activity activity;

    public PointHistoryAdapter(ArrayList<PointHistory> pointsHistory, Activity activity) {
        this.pointsHistory = pointsHistory;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pointhistorycard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int point_id = pointsHistory.get(position).getPoint_id();

        HistoryViewModel hvm = new ViewModelProvider((ViewModelStoreOwner) activity).get(HistoryViewModel.class);
        LiveData<FullPointOfInterest> livePoint = hvm.getPointById(point_id);

        livePoint.observe((LifecycleOwner) this.activity, new Observer<FullPointOfInterest>() {
            @Override
            public void onChanged(FullPointOfInterest fullPoint) {

                if(fullPoint != null) {
                    PointOfInterest point = fullPoint.getPoint();

                    String description = point.getPin_desc();
                    String descritionLimit = description.substring(0, Math.min(description.length(), 200)) + "...";

                    Date date = pointsHistory.get(holder.getAdapterPosition()).getDate();

                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()); // get a localized date format based on the user's locale
                    String dateString = formatter.format(date);

                    holder.pointName.setText(point.getPin_name());
                    holder.pointDescription.setText(descritionLimit);
                    holder.date.setText(dateString);
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, PointOfInterestActivity.class);
                            intent.putExtra("pointID",point.getId());
                            activity.startActivity(intent);
                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return pointsHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView((R.id.pointName))
        TextView pointName;
        @BindView((R.id.pointDescription))
        TextView pointDescription;
        @BindView((R.id.pointDate))
        TextView date;
        @BindView((R.id.pointHistoryCard))
        RelativeLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setPointsHistory(ArrayList<PointHistory> points) {
        this.pointsHistory = points;
    }

}
