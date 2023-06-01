package com.example.fase1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.fase1.ViewModel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel lvm;

    @BindView((R.id.inputUser))
    EditText editTextUser;
    @BindView((R.id.inputPass))
    EditText editTextPassword;
    @BindView((R.id.ConfirmButton))
    Button confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("shared preferences",MODE_PRIVATE);
        if (sp.getString("cookies", null) != null) {  // User is already logged in
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        else { // User is not logged in
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

            lvm = new ViewModelProvider(this).get(LoginViewModel.class);
            confirmButton.setOnClickListener(view -> lvm.postLogin(editTextUser.getText().toString(),
                                                                   editTextPassword.getText().toString(),
                                                                   findViewById(R.id.textView)));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = getSharedPreferences("shared preferences",MODE_PRIVATE);
        if (sp.getString("cookies", null) != null) {  // User is already logged in
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {}

}