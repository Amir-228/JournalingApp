package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class addEntry extends AppCompatActivity {

    EditText title, input;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addentrymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.save){
            saveToDatabase();
            return true;
        }
        else if(item.getItemId() == R.id.cancel){
            goHome();
            return true;
        }
        else if(item.getItemId() == R.id.home) {
            goHome();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        title = findViewById(R.id.titleInput);
        input = findViewById(R.id.entryInputText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loadSharedPreferences();

    }
    public void goHome(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void saveToDatabase(){

        String journalTitle = title.getText().toString().trim();
        String journalContent = input.getText().toString().trim();
        String ownerName = "Owner name";
        Date date = new Date();
        userId = fAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> data = new HashMap<>();
        data.put("UserID",userId);
        data.put("Title",journalTitle);
        data.put("Content",journalContent);
        data.put("Owner", ownerName);
        data.put("Date", date);

        db.collection("journals")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(addEntry.this, "Journal added to the Database.",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addEntry.this, "Failed to upload to the Database.",Toast.LENGTH_SHORT).show();
            }
        });

        startActivity(new Intent(getApplicationContext(), addEntry.class));
        finish();

    }

    public void loadSharedPreferences() {

        SharedPreferences sp = getSharedPreferences(appSettingsActivity.SHARED_PREFS, MODE_PRIVATE);

        ConstraintLayout layoutm = findViewById(R.id.addEntryLayout);

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