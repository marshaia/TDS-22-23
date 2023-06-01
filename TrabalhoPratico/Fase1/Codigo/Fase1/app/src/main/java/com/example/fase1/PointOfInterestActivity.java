package com.example.fase1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.InfoPoint;
import com.example.fase1.ViewModel.PointViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PointOfInterestActivity extends AppCompatActivity {

    private FullPointOfInterest pin;
    private PointViewModel pvm;
    private Boolean isPremium = false;


    @BindView((R.id.pin_addToHistoryBtn))
    Button addToHistoryButton;

    @BindView((R.id.pin_MediaButton))
    Button mediaButton;

    @BindView((R.id.pin_thumbnail))
    ImageView pinThumbnail;
    @BindView((R.id.pin_name))
    TextView pinName;
    @BindView((R.id.pin_description))
    TextView pinDescription;
    @BindView((R.id.pin_ExtrasLabel))
    TextView pinExtraLabel;
    @BindView((R.id.pin_Extras))
    TextView pinExtras;
    @BindView((R.id.pin_location))
    TextView mapsLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);
        ButterKnife.bind(this);
        setTitle("Pin");

        // getting the point ID from Intent content
        Intent intent = getIntent();
        int id = intent.getIntExtra("pointID", 0);

        pvm = new ViewModelProvider(this).get(PointViewModel.class);
        isPremium = pvm.getPremium();
        pvm.setup(id);
        LiveData<FullPointOfInterest> livePoint = pvm.getPoint();

        livePoint.observe(this, point -> {
            pin = point;
            if (pin != null) setPinInfo();
        });

        mediaButton();
        historyButton();
    }


    public void historyButton () {
        if (!isPremium) addToHistoryButton.setVisibility(View.GONE);
        else {
            addToHistoryButton.setVisibility(View.VISIBLE);
            addToHistoryButton.setOnClickListener(view -> {
                pvm.addPointToHistory(pin.getPoint().getId());
                Toast.makeText(PointOfInterestActivity.this, "Pin added to history!", Toast.LENGTH_SHORT).show();
            });
        }
    }


    public void mediaButton () {
        if (!isPremium) mediaButton.setVisibility(View.GONE);
        else {
            mediaButton.setVisibility(View.VISIBLE);
            mediaButton.setOnClickListener(view -> {
                Intent intent = new Intent(PointOfInterestActivity.this, MediaActivity.class);
                intent.putExtra("pointID", pin.getPoint().getId());
                startActivity(intent);
            });
        }
    }


    public void setPinInfo () {

        setThumbNail();

        pinExtraLabel.setVisibility(View.GONE);
        pinExtras.setVisibility(View.GONE);

        pinName.setText(this.pin.getPoint().getPin_name());
        pinDescription.setText(this.pin.getPoint().getPin_desc());

        setMapsLink();

        // If it has rel_pin, displays it
        if (this.pin.getInfo().size() > 0) {
            pinExtraLabel.setVisibility(View.VISIBLE);
            pinExtras.setVisibility(View.VISIBLE);
            StringBuilder fullExtras = new StringBuilder("<ul>");
            for(InfoPoint r : this.pin.getInfo()) {
                fullExtras.append("<li><b>").append(r.getAttrib()).append(":</b>");
                fullExtras.append(" ").append(r.getValue()).append("</li>");
            }
            fullExtras.append("</ul>");
            Spanned spannedExtras = HtmlCompat.fromHtml(fullExtras.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY);
            pinExtras.setText(spannedExtras);
        }
    }

    public void setMapsLink () {
        mapsLink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString link = new SpannableString("Location");

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.google.com/maps/search/?api=1");

        // Set location point
        String location = pin.getPoint().getPin_lat() + "," + pin.getPoint().getPin_lng();
        urlBuilder.append("&query=").append(location);
        String url = urlBuilder.toString();

        link.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        },0,link.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mapsLink.setText(link);
    }

    public void setThumbNail(){

        if (this.pin.getPoint().getThumbnailURL() != null){
            String link = this.pin.getPoint().getThumbnailURL();
            pinThumbnail.setImageBitmap(getBitMap(link));
        }

    }


    public Bitmap getBitMap(String imageUrl){

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        File file = new File(this.getFilesDir(), filename);
        Bitmap image = null;
        if (file.exists())
            image = BitmapFactory.decodeFile(file.getAbsolutePath());

        return image;
    }

}