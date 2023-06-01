package com.example.fase1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @BindView((R.id.bottom_navigation))
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("shared preferences",MODE_PRIVATE);
        if (sp.getString("cookies", null) != null) {  // User is already logged in
            setContentView(getLayoutResourceId());
            ButterKnife.bind(this);

            bottomNavigationView.setOnItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(getNavBarItemSelected());

            afterNavBar();
        }

        else startLoginActivity();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.navBar_contactos):
                if (getLayoutResourceId() != R.layout.activity_contacts) {
                    Intent searchIntent = new Intent(this, ContactsActivity.class);
                    startActivity(searchIntent);
                }
                return true;

            case (R.id.navBar_homepage):
                if (getLayoutResourceId() != R.layout.activity_main) {
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                }
                return true;

            case (R.id.navBar_perfil):
                if (getLayoutResourceId() != R.layout.activity_profile) {
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
                return true;

            case (R.id.navBar_roteiros):
                if (getLayoutResourceId() != R.layout.activity_trails) {
                    Intent trailsIntent = new Intent(this, TrailsActivity.class);
                    startActivity(trailsIntent);
                }
                return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Verifica se o user está logged in ou não
        SharedPreferences sp = getSharedPreferences("shared preferences",MODE_PRIVATE);
        if (sp.getString("cookies", null) != null) bottomNavigationView.setSelectedItemId(getNavBarItemSelected());
        else startLoginActivity();
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected abstract int getLayoutResourceId();
    protected abstract int getNavBarItemSelected();
    protected abstract void afterNavBar(); // use for additional code on onCreate method
}

