package com.example.fase1;

import static com.example.fase1.SingleTrailActivity.CHANNEL_ID;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.fase1.Data.App;
import com.example.fase1.Data.FullApp;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.Partner;
import com.example.fase1.Data.Social;
import com.example.fase1.Data.User;
import com.example.fase1.Services.NotificationService;
import com.example.fase1.ViewModel.HomeViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView((R.id.home_appName))
    TextView appName;
    @BindView((R.id.home_appDescription))
    TextView appDescription;
    @BindView((R.id.home_appDesc))
    TextView appSlogan;
    @BindView((R.id.home_socialsName))
    TextView appSocials;
    @BindView((R.id.home_partnerName))
    TextView appPartnerName;
    @BindView((R.id.home_partnerCell))
    TextView appPartnerCell;
    @BindView((R.id.home_partnerEmail))
    TextView appPartnerEmail;

    @BindView((R.id.home_PinName))
    TextView pinName;
    @BindView((R.id.home_PinDescription))
    TextView pinDescription;
    @BindView((R.id.home_PinThumbnail))
    ImageView pinImage;

    @BindView((R.id.home_mapsText))
    TextView mapsText;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }
    protected int getNavBarItemSelected () {
        return R.id.navBar_homepage;
    }

    @Override
    protected void afterNavBar() {
        ButterKnife.bind(this);
        setTitle("BraGuia");
        checkTrail();
        setAppInfo();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void checkTrail(){
        SharedPreferences sp2 = getSharedPreferences("shared preferences",MODE_PRIVATE);
        int trail = sp2.getInt("trail_id", 0);
        float trailDistance = sp2.getFloat("trailDistance",0);
        float trailTime = sp2.getFloat("trailTime",0);
        float last_lat = sp2.getFloat("last_lat",0);
        float last_lng = sp2.getFloat("last_lng",0);
        List<FullPointOfInterest> points = getPoints();

        if(trail != 0 && !isMyServiceRunning(NotificationService.class)){
            Log.e("MAIN", "TRAIL EXISTS");
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("points", (Serializable) points);
            intent.putExtra("trail_id",trail);
            intent.putExtra("trailDistance", trailDistance);
            intent.putExtra("trailTime", trailTime);
            intent.putExtra("last_lat",last_lat);
            intent.putExtra("last_lng",last_lng);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }

        }
    }


    public List<FullPointOfInterest> getPoints() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        String myObjectsJson = sharedPreferences.getString("points", null);

        Gson gson = new Gson();
        Type type = new TypeToken<List<FullPointOfInterest>>() {}.getType();

        return gson.fromJson(myObjectsJson, type);
    }


    public void setAppInfo () {

        HomeViewModel avm = new ViewModelProvider(this).get(HomeViewModel.class);

        LiveData<List<FullApp>> fullAppList = avm.getFullApp();
        fullAppList.observe(this, fullApps -> {
            FullApp fullApp = fullApps.get(0);
            App app = fullApps.get(0).getApp();
            appName.setText(app.getName());
            appDescription.setText(app.getApp_landing_page_text());
            appSlogan.setText(app.getDesc());
            setMapsLink();

            setSocials(fullApp);
            setPartners(fullApp);
        });

        LiveData<List<FullTrail>> trailsLive = avm.getTrails();
        trailsLive.observe(this, fullTrails -> {
        });

        LiveData<List<FullPointOfInterest>> livePoint = avm.getPoints();
        livePoint.observe(this, points -> {
            if(points.size()>0) {
                int max = points.size();
                int min = 0;
                int int_random = (int) ((Math.random() * (max - min)) + min);

                setPointInfo((points.get(int_random)));
            }
        });

        LiveData<List<User>> usersLive = avm.getAllUsers();
        usersLive.observe(this, users -> {
            User user = users.get(0);
            boolean isPremium = user.getType().equals("Premium");

            SharedPreferences sp = getApplication().getSharedPreferences("shared preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("usertype",isPremium);
            editor.apply();
        });
    }

    public void setPointInfo (FullPointOfInterest point) {

        String desc = point.getPoint().getPin_desc();
        String subDesc = desc.substring(0, Math.min(desc.length(), 250)) + "...";

        pinName.setText(point.getPoint().getPin_name());
        pinDescription.setText(subDesc);

        String link = point.getPoint().getThumbnailURL();

        if (link != null) pinImage.setImageBitmap(getBitMap(link));
        else Picasso.get().load(R.drawable.location);

        pinDescription.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,PointOfInterestActivity.class);
            intent.putExtra("pointID", point.getPoint().getId());
            startActivity(intent);
        });
        pinImage.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,PointOfInterestActivity.class);
            intent.putExtra("pointID", point.getPoint().getId());
            startActivity(intent);
        });
    }

    public Bitmap getBitMap(String imageUrl){

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        File file = new File(this.getFilesDir(), filename);
        Bitmap image = null;
        if (file.exists())
            image = BitmapFactory.decodeFile(file.getAbsolutePath());

        return image;
    }


    public void setSocials (FullApp fullApp) {
        if(fullApp.getSocials().size()>0) {
            Social social = fullApp.getSocials().get(0);
            setSpannableLink(social.getName(),social.getUrl(), appSocials);
        }
    }


    public void setPartners (FullApp fullApp) {
        if(fullApp.getPartners().size()>0) {
            Partner partner = fullApp.getPartners().get(0);
            setSpannableLink(partner.getName(), partner.getUrl(), appPartnerName);

            appPartnerCell.setText(partner.getPhone());
            appPartnerEmail.setText(partner.getMail());
        }
    }


    public void setMapsLink () {
        mapsText.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString link = new SpannableString("Google Maps");
        link.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps"));
                startActivity(intent);
            }
        },0,link.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String maps1 = getString(R.string.homeMapsWarning1) + " ";
        String maps2 = " " + getString(R.string.homeMapsWarning2);

        mapsText.setText(TextUtils.concat(maps1,link,maps2));
    }


    public void setSpannableLink (String string, String url, TextView view) {
        view.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString link = new SpannableString(string);
        link.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        },0,link.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        view.setText(link);
    }

}