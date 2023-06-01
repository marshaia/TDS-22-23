package com.example.fase1.Repositories;

import android.app.Application;
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
import com.example.fase1.DAO.AppDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.App;
import com.example.fase1.Data.FullApp;
import com.example.fase1.Data.Partner;
import com.example.fase1.Data.Social;
import com.example.fase1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {

    private final Application app;
    private final AppDAO appDAO;
    private MediatorLiveData<List<FullApp>> fullApp;

    public AppRepository(Application application) {
        app = application;
        RoomDB db = RoomDB.getDatabase(application);
        appDAO = db.appDAO();
        fullApp = new MediatorLiveData<>();
        fullApp.addSource(
            appDAO.getApp(), localContacts -> {
                if (localContacts != null && localContacts.size() > 0) {
                    fullApp.setValue(localContacts);
                } else {
                    getDataFromAPI();
                }
            }
        );
    }


    public void insert(App app, List<Partner> partners, List<Social> socials) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            appDAO.insertApp(app);
            appDAO.insertPartners(partners);
            appDAO.insertSocials(socials);
        });
    }

    public LiveData<List<FullApp>> getApp(){
        return fullApp;
    }

    public void getDataFromAPI(){

        String url = app.getString(R.string.urlApp);

        RequestQueue queue = Volley.newRequestQueue(app);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    RoomDB.databaseWriteExecutor.execute(() -> {
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            App app = new App(
                                    0,
                                    obj.getString("app_name"),
                                    obj.getString("app_desc"),
                                    obj.getString("app_landing_page_text")
                            );

                            List<Social> socials = getSocials(obj.getJSONArray("socials"));
                            List<Partner> partners = getPartners(obj.getJSONArray("partners"));

                            appDAO.insertApp(app);
                            appDAO.insertSocials(socials);
                            appDAO.insertPartners(partners);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Toast.makeText(app, "Network Error", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ServerError) {
                        Toast.makeText(app, "ServerError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(app, "AuthFailureError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(app, "ParseError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(app, "TimeoutError", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(app, "Something else went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }


    public List<Social> getSocials(JSONArray jsonSocials) throws JSONException {

        ArrayList<Social> tmp = new ArrayList<>();

        for (int i = 0; i < jsonSocials.length(); i++) {
            try {
                JSONObject socialObject = jsonSocials.getJSONObject(i);

                Social social = new Social(
                        i,
                        0,
                        socialObject.getString("social_name"),
                        socialObject.getString("social_url"),
                        socialObject.getString("social_share_link"));

                tmp.add(social);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }


    public List<Partner> getPartners(JSONArray jsonPartners) throws JSONException {

        ArrayList<Partner> tmp = new ArrayList<>();

        for (int i = 0; i < jsonPartners.length(); i++) {
            try {
                JSONObject partnerObject = jsonPartners.getJSONObject(i);

                Partner partner = new Partner(
                        i,
                        0,
                        partnerObject.getString("partner_name"),
                        partnerObject.getString("partner_phone"),
                        partnerObject.getString("partner_url"),
                        partnerObject.getString("partner_mail"),
                        partnerObject.getString("partner_desc"));

                tmp.add(partner);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

}
