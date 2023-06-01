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
import com.example.fase1.DAO.ContactDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.Contact;
import com.example.fase1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContactRepository {

    private final Application app;
    private final ContactDAO contactDAO;
    private MediatorLiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        app = application;
        RoomDB db = RoomDB.getDatabase(application);
        contactDAO = db.contactDAO();
        allContacts = new MediatorLiveData<>();
        allContacts.addSource(
                contactDAO.getContacts(), localContacts -> {
                    if (localContacts != null && localContacts.size() > 0) {
                        allContacts.setValue(localContacts);
                    } else {
                        getDataFromAPI();
                    }
                }
        );
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public void insert(Contact contact) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            contactDAO.insert(contact);
        });
    }

    public void delete(Contact contact){
        RoomDB.databaseWriteExecutor.execute(() -> {
            contactDAO.delete(contact);
        });
    }

    public void getDataFromAPI(){

        AtomicInteger id = new AtomicInteger();

        String url = app.getApplicationContext().getString(R.string.urlApp);

        RequestQueue queue = Volley.newRequestQueue(app.getApplicationContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null,
                response -> {
                    try {
                        JSONArray obj = response.getJSONObject(0).getJSONArray("contacts");

                        for (int i = 0; i < obj.length(); i++) {

                            JSONObject c = obj.getJSONObject(i);

                            Contact contact = new Contact(
                                    id.get(),
                                    c.getString("contact_name"),
                                    c.getInt("contact_phone"),
                                    c.getString("contact_url"),
                                    c.getString("contact_mail"),
                                    c.getString("contact_desc"),
                                    c.getString("contact_app"));

                            insert(contact);
                            id.getAndIncrement();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
}

