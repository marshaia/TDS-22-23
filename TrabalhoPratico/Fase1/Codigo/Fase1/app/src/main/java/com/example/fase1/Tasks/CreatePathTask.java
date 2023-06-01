package com.example.fase1.Tasks;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;

import com.example.fase1.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;

public class CreatePathTask extends AsyncTask<String[], Void, DirectionsResult> {

    private final Application app;
    private final GoogleMap googleMap;
    private final String origin;
    private final String destination;

    public CreatePathTask(Application app, GoogleMap googleMap,String origin, String destination) {
        this.app = app;
        this.googleMap = googleMap;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    protected DirectionsResult doInBackground(String[]... params) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(app.getString(R.string.APIKey))
                .build();
        try {
            return DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(origin).waypoints(params[0])
                    .destination(destination)
                    .await();
        } catch ( IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (com.google.maps.errors.ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPolyline(DirectionsResult result, GoogleMap googleMap) {
        List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        PolylineOptions path = new PolylineOptions().addAll(decodedPath);
        googleMap.addPolyline(path.color(Color.BLUE).width(5));

    }


    @Override
    protected void onPostExecute(DirectionsResult result) {
        if (result != null && result.routes.length > 0) {
            addPolyline(result, googleMap);
        }
    }
}