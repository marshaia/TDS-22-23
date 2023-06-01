package com.example.fase1.ViewModel;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.Media;
import com.example.fase1.Repositories.PointOfInterestRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MediaViewModel extends AndroidViewModel {

    private PointOfInterestRepository pointRepository;
    private Application app;

    private LiveData<FullPointOfInterest> point;

    public MediaViewModel(Application application) {
        super(application);
        this.app = application;
        pointRepository = new PointOfInterestRepository(application);
    }

    public void setup(int id){
        point = pointRepository.getPointById(id);
    }

    public LiveData<FullPointOfInterest> getPoint(){
        return point;
    }

    public void downloadImage(Media m){

        String imageUrl = m.getMedia_file();

        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        FileOutputStream fos = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(inputStream);

            fos = app.openFileOutput(filename, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void download(Media m){

        String url = m.getMedia_file();
        String filename = url.substring(url.lastIndexOf("/") + 1);

        DownloadManager downloadManager = (DownloadManager) app.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("My Download");
        request.setDescription("Downloading file...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(app, Environment.DIRECTORY_DOWNLOADS, filename);
        downloadManager.enqueue(request);

    }



}