package com.example.fase1.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.PointOfInterest;
import com.example.fase1.PointOfInterestActivity;
import com.example.fase1.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointsTrailAdapter extends RecyclerView.Adapter<PointsTrailAdapter.ViewHolder> {

    private List<FullPointOfInterest> pointsList;
    private final Activity activity;
    private final Application application;
    private final Boolean isPremium;

    public PointsTrailAdapter(List<FullPointOfInterest> points, Activity activity, Application application,Boolean isPremium){
        this.pointsList = points;
        this.activity = activity;
        this.application = application;
        this.isPremium = isPremium;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pointofinteresttrailcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PointOfInterest point = pointsList.get(position).getPoint();
        holder.title.setText(point.getPin_name());
        setTextViewFontSize(holder.title, 200, point.getPin_name()); // 200 is the maximum width of the TextView

        holder.description.setText(point.getPin_desc());

        if (point.getThumbnailURL() != null){
            holder.image.setImageBitmap(getBitMap(point.getThumbnailURL()));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PointOfInterestActivity.class);
                intent.putExtra("pointID",point.getId());
                intent.putExtra("isPremium", isPremium);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pointsList != null ? pointsList.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView((R.id.pName))
        TextView title;
        @BindView((R.id.pDescription))
        TextView description;
        @BindView((R.id.imageView5))
        ImageView image;
        @BindView((R.id.pitrailCard))
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setPointsList(List<FullPointOfInterest> points) {
        this.pointsList = points;
    }

    public Bitmap getBitMap(String imageUrl){

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        File file = new File(application.getFilesDir(), filename);
        Bitmap image = null;
        if (file.exists())
            image = BitmapFactory.decodeFile(file.getAbsolutePath());

        return image;
    }

    public void setTextViewFontSize(TextView textView, float maxWidth, String text) {
        float textSize = 20; // starting font size
        float density = textView.getResources().getDisplayMetrics().density;
        Rect bounds = new Rect();
        Paint paint = textView.getPaint();
        paint.setTextSize(textSize * density);
        paint.getTextBounds(text, 0, text.length(), bounds);
        while (bounds.width() > maxWidth * density ) {
            textSize--;
            paint.setTextSize(textSize * density);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }
}
