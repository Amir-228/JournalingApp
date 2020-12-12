package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText userName, password;
    Button login;
    TextView goToRegister;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        goToRegister = findViewById(R.id.goToRegister);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = userName.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailValue)) {
                    userName.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(passwordValue)) {
                    password.setError("Password is Required.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "Logged in Successfully. ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "The User Name or Password is invalid! Please try again.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        loadSharedPreferences();

    }

    public void loadSharedPreferences() {

        SharedPreferences sp = getSharedPreferences(appSettingsActivity.SHARED_PREFS, MODE_PRIVATE);

        ConstraintLayout layoutm = findViewById(R.id.loginLayout);

        switch(sp.getInt(appSettingsActivity.BACKGROUND_CHOICE, 0))    {

            case 0:
                layoutm.setBackground(getDrawable(R.drawable.bglogin));
                break;

            case 1:
                layoutm.setBackgroundColor(sp.getInt(appSettingsActivity.BACKGROUND_ONEONE, 0));
                break;

            case 2:
                GradientDrawable gd = new GradientDrawable();

                gd.setOrientation(GradientDrawable.Orientation.BL_TR);

                gd.setColors(new int[]  {sp.getInt(appSettingsActivity.BACKGROUND_ONETWO, 0), sp.getInt(appSettingsActivity.BACKGROUND_TWOTWO, 0)});

                layoutm.setBackground(gd);
                break;

            case 3:
                layoutm.setBackground(getDrawable(R.drawable.gradient_list));

                AnimationDrawable animationDrawable = (AnimationDrawable) layoutm.getBackground();
                animationDrawable.setEnterFadeDuration(2000);
                animationDrawable.setExitFadeDuration(4000);
                animationDrawable.start();
                break;
        }
    }

}