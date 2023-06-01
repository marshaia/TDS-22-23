package com.example.fase1;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.fase1.Data.User;
import com.example.fase1.ViewModel.ProfileViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileActivity extends BaseActivity{

    private User user;

    @BindView((R.id.profile_favorites))
    Button favorites;
    @BindView((R.id.profile_history))
    Button profileHistory;
    @BindView((R.id.profile_LogoutBtn))
    ImageView logout;
    @BindView(R.id.profile_settingBtn)
    ImageView settingBtn;
    @BindView((R.id.profile_PremiumLogo))
    ImageView premiumLogo;
    @BindView((R.id.profile_FirstLastName))
    TextView userFullName;
    @BindView((R.id.profile_username))
    TextView username;
    @BindView((R.id.profile_userEmail))
    TextView userEmail;
    @BindView((R.id.profile_EmailLabel))
    TextView userEmailLabel;

    private ProfileViewModel pvm;
    private Boolean isPremium;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }
    @Override
    protected int getNavBarItemSelected() {
        return R.id.navBar_perfil;
    }
    @Override
    protected void afterNavBar() {
        setTitle("Profile");
        ButterKnife.bind(this);

        pvm = new ViewModelProvider(this).get(ProfileViewModel.class);
        LiveData<List<User>> usersLive = pvm.getUsers();
        usersLive.observe(this, users -> {
            if(users.size()>0) {
                user = users.get(0);
                setProfileInfo();
            }
        });
    }


    public void setButtonListeners () {

        if (isPremium) {
            profileHistory.setVisibility(View.VISIBLE);
            profileHistory.setOnClickListener(view -> {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            });

            settingBtn.setVisibility(View.VISIBLE);
            settingBtn.setOnClickListener(view -> {
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
            });
        }
        else {
            profileHistory.setVisibility(View.GONE);
            settingBtn.setVisibility(View.GONE);
        }

        logout.setOnClickListener(view -> logOut());

        favorites.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }


    public void setProfileInfo () {

        isPremium = pvm.getPremium();

        if (isPremium) premiumLogo.setVisibility(View.VISIBLE);
        else premiumLogo.setVisibility(View.GONE);

        setButtonListeners();

        String fullname = user.getFirst_name() + " " + user.getLast_name();
        userFullName.setText(fullname);
        String user_name = "@" + user.getUsername();
        username.setText(user_name);

        if (user.getEmail().length() > 0){
            userEmail.setText(user.getEmail());
        }
        else {
            userEmail.setVisibility(View.GONE);
            userEmailLabel.setVisibility(View.GONE);
        }
    }


    public void logOut () {
        pvm.logout();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

}
