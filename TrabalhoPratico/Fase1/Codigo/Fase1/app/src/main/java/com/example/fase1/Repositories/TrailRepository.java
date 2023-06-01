package com.example.fase1.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fase1.DAO.TrailDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.Edge;
import com.example.fase1.Data.FullPointOfInterest;
import com.example.fase1.Data.FullTrail;
import com.example.fase1.Data.InfoPoint;
import com.example.fase1.Data.InfoTrail;
import com.example.fase1.Data.Media;
import com.example.fase1.Data.PointOfInterest;
import com.example.fase1.Data.Trail;
import com.example.fase1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TrailRepository {

    private final Application app;
    private final TrailDAO trailDAO;
    private MediatorLiveData<List<FullTrail>> allTrails;

    public TrailRepository(Application application) {
        app = application;
        RoomDB db = RoomDB.getDatabase(application);
        trailDAO = db.trailDAO();
        allTrails = new MediatorLiveData<>();
        allTrails.addSource(
            trailDAO.getTrails(), localTrails -> {
                if (localTrails != null && localTrails.size() > 0) {
                    allTrails.setValue(localTrails);
                    checkIfRefetchIsNeeded();
                } else {
                    getDataFromAPI();
                    saveTimeOfFetch();
                }
            }
        );

    }

    public void checkIfRefetchIsNeeded(){
        long lastFetch = getTimeOfLastFetch();

        long currentTime = System.currentTimeMillis();

        long timeSinceLastUpdate = currentTime - lastFetch;
        long updateInterval = 3 * 24 * 60 * 60 * 1000; // 3 days in milliseconds

        if (timeSinceLastUpdate > updateInterval) {
            getDataFromAPI();
            saveTimeOfFetch();
        }
    }

    public void saveTimeOfFetch() {
        SharedPreferences sp = app.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("last_fetch_time", System.currentTimeMillis());
        editor.apply();
    }

    public long getTimeOfLastFetch(){
        SharedPreferences sp = app.getSharedPreferences("shared preferences", MODE_PRIVATE);
        return sp.getLong("last_fetch_time", 0);
    }

    public LiveData<List<FullTrail>> getAllTrails() {
        return allTrails;
    }

    public void insert(Trail trail, List<Edge> edges, List<InfoTrail> infos) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            trailDAO.insertTrail(trail);
            trailDAO.insertEdges(edges);
            trailDAO.insertTrailInfos(infos);
        });
    }

    public LiveData<List<FullPointOfInterest>> getTrailPoints(int id){
        return trailDAO.getTrailPoints(id);
    }

    public LiveData<FullTrail> getTrailById(int id){
        return trailDAO.getTrailById(id);
    }

    public void getDataFromAPI(){

        String url = app.getApplicationContext().getString(R.string.urlTrails);

        RequestQueue queue = Volley.newRequestQueue(app.getApplicationContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null,
                response -> {
                    RoomDB.databaseWriteExecutor.execute(() -> {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                Trail t = new Trail(
                                        obj.getInt("id"),
                                        obj.getString("trail_img"),
                                        obj.getString("trail_name"),
                                        obj.getString("trail_desc"),
                                        obj.getInt("trail_duration"),
                                        obj.getString("trail_difficulty"));

                                saveImageLocally(obj.getString("trail_img"));

                                ArrayList<InfoTrail> infos = getTrailInfos(obj.getJSONArray("rel_trail"));
                                ArrayList<Edge> edges = getEdges(obj.getJSONArray("edges"));

                                trailDAO.insertTrail(t);
                                trailDAO.insertTrailInfos(infos);
                                trailDAO.insertEdges(edges);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Toast.makeText(app.getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ServerError) {
                        Toast.makeText(app.getApplicationContext(), "ServerError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(app.getApplicationContext(), "AuthFailureError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(app.getApplicationContext(), "ParseError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(app.getApplicationContext(), "TimeoutError", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(app.getApplicationContext(), "Something else went wrong", Toast.LENGTH_SHORT).show();
                    }

                });

        queue.add(request);

    }

    public ArrayList<Edge> getEdges(JSONArray edges){

        ArrayList<Edge> tmp = new ArrayList<>();

        for (int i = 0; i < edges.length(); i++) {
            try {
                JSONObject edgeObject = edges.getJSONObject(i);

                Edge edge = new Edge(
                        edgeObject.getInt("id"),
                        getPointId(edgeObject.getJSONObject("edge_start")),
                        getPointId(edgeObject.getJSONObject("edge_end")),
                        edgeObject.getString("edge_transport"),
                        edgeObject.getInt("edge_duration"),
                        edgeObject.getString("edge_desc"),
                        edgeObject.getInt("edge_trail"));

                tmp.add(edge);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tmp;
    }

    public int getPointId(JSONObject o) throws JSONException {

        PointOfInterest p;

        try {
            p = new PointOfInterest(
                    o.getInt("id"),
                    o.getString("pin_name"),
                    o.getString("pin_desc"),
                    o.getDouble("pin_lat"),
                    o.getDouble("pin_lng"),
                    o.getDouble("pin_alt"));

            getPointInfos(o.getJSONArray("rel_pin"));
            ArrayList<Media> medias = getMedias(o.getJSONArray("media"));

            String thumbnail = getThumbNailUrl(medias);

            if(thumbnail != null){
                p.setThumbnailURL(thumbnail);
                saveImageLocally(thumbnail);
            }

            trailDAO.insertPoint(p);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o.getInt("id");
    }

    public ArrayList<Media> getMedias(JSONArray media){

        ArrayList<Media> tmp = new ArrayList<>();

        for (int i = 0; i < media.length(); i++) {
            try {
                JSONObject mediaObject = media.getJSONObject(i);

                Media m = new Media(
                        mediaObject.getInt("id"),
                        mediaObject.getString("media_file").replace("http://","https://"),
                        mediaObject.getString("media_type"),
                        mediaObject.getInt("media_pin"));

                tmp.add(m);

                trailDAO.insertMedia(m);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tmp;
    }

    public ArrayList<InfoTrail> getTrailInfos(JSONArray infos){

        ArrayList<InfoTrail> tmp = new ArrayList<>();

        for (int i = 0; i < infos.length(); i++) {
            try {
                JSONObject infoObject = infos.getJSONObject(i);

                InfoTrail infoTrail = new InfoTrail(
                        infoObject.getInt("id"),
                        infoObject.getString("value"),
                        infoObject.getString("attrib"),
                        infoObject.getInt("trail"));

                tmp.add(infoTrail);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tmp;
    }

    public void getPointInfos(JSONArray infos){

        for (int i = 0; i < infos.length(); i++) {
            try {
                JSONObject infoObject = infos.getJSONObject(i);

                InfoPoint infoPoint = new InfoPoint(
                        infoObject.getInt("id"),
                        infoObject.getString("value"),
                        infoObject.getString("attrib"),
                        infoObject.getInt("pin"));

                trailDAO.insertPointInfo(infoPoint);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveImageLocally(String imageUrl){

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

    public String getThumbNailUrl (List<Media> medias) {
        for (Media media : medias) {
            if (media.getMedia_type().equals("I")) {
                return media.getMedia_file();
            }
        }
        return null;
    }

    public LiveData<List<Integer>> getTrailsStarted(){
        return trailDAO.getTrailsStarted();
    }

}