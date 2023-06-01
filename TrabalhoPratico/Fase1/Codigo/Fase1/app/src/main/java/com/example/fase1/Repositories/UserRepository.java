package com.example.fase1.Repositories;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fase1.DAO.UserDAO;
import com.example.fase1.DB.RoomDB;
import com.example.fase1.Data.User;
import com.example.fase1.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final Application app;

    private final UserDAO userDAO;
    private MediatorLiveData<List<User>> users;

    private String cookies;

    public UserRepository(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        app = application;
        userDAO = db.userDAO();
        users = new MediatorLiveData<>();

        users.addSource(
            userDAO.getUsers(), localUsers -> {
                if (localUsers != null && localUsers.size() > 0) {
                    users.setValue(localUsers);

                } else {
                    getDataFromAPI();
                }
            }
        );
    }

    public LiveData<List<User>> getUsers(){
        return users;
    }

    public void insert(User user) {
        RoomDB.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public void delete(){
        RoomDB.databaseWriteExecutor.execute(userDAO::deleteAll);
    }

    public void getDataFromAPI() {

        getCookiesAndSetHeader();

        String url = app.getApplicationContext().getString(R.string.urlProfile);

        RequestQueue queue = Volley.newRequestQueue(app.getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                try {
                        User user = new User(
                                response.getString("user_type"),
                                response.getString("username"),
                                response.getString("first_name"),
                                response.getString("last_name"),
                                response.getString("email")
                        );

                        insert(user);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Toast.makeText(this.app, "Network Error", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ServerError) {
                        Toast.makeText(this.app, "ServerError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(this.app, "AuthFailureError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(this.app, "ParseError", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(this.app, "TimeoutError", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this.app, "Something else went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() {
                Map<String, String> h = new HashMap<>();
                h.put("Cookie", cookies);
                return h;
            }
        };

        queue.add(request);
    }


    public void getCookiesAndSetHeader(){
        SharedPreferences sp2 = app.getSharedPreferences("shared preferences", MODE_PRIVATE);
        this.cookies = sp2.getString("cookies",null);

        if(cookies == null) cookies = "";
    }

}
