package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import yuku.ambilwarna.AmbilWarnaDialog;

public class appSettingsActivity extends AppCompatActivity {

    int mDefaultColor = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settingsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Home:
                goHome();
                return true;
            default:
                return false;
        }

    }


    public void colorTwoTwo(View view) {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;

            }
        });
    }

    public void colorOneTwo(View view) {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;

            }
        });
    }

    public void colorOneOne(View view) {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;

            }
        });
    }

    public void goHome(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}