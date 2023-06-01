package com.example.fase1.ViewModel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fase1.MainActivity;
import com.example.fase1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class LoginViewModel extends AndroidViewModel {

    private String cookiesString = "";
    private final Application app;


    public LoginViewModel(Application application) {
        super(application);
        this.app = application;
    }


    public void postLogin (String username, String password, TextView errorView) {

        String url = app.getString(R.string.urlLogin);

        RequestQueue requestQueue = Volley.newRequestQueue(app);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("email", "");
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> startMainActivity(),
                error -> errorView.setText(R.string.errorMessage)) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                List<Header> headers = response.allHeaders;

                assert headers != null;
                for (Header h : headers) {
                    String[] values = h.getValue().split(";");
                    if (values.length >= 2 && (values[0].split("=")[0].equals("sessionid") || values[0].split("=")[0].equals("csrftoken"))) {
                        cookiesString = cookiesString + values[0] + ';';
                    }
                }
                saveCookies();
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    public void saveCookies(){
        SharedPreferences sp = getApplication().getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cookies",cookiesString);
        editor.apply();
    }


    public void startMainActivity ()  {
        Intent intent = new Intent(app, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }

}
