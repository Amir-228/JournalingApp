package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class editActivity extends AppCompatActivity {

    EditText title, input;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, oldTitle;

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
            saveToDatabase(oldTitle);
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

        title = findViewById(R.id.titleInput2);
        input = findViewById(R.id.entryInputText2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Intent rec = getIntent();
        title.setText(rec.getStringExtra("title"));
        input.setText(rec.getStringExtra("content"));
        oldTitle = title.getText().toString();

        loadSharedPreferences();
    }

    public void goHome(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void saveToDatabase(String oldTitle){

        String journalTitle = title.getText().toString().trim();
        String journalContent = input.getText().toString().trim();

        Map<String,Object> data = new HashMap<>();
        data.put("Title",journalTitle);
        data.put("Content",journalContent);

        //update the database.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("journals").document(oldTitle)
                //.update("Title", title.getText().toString())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editActivity.this, "Journal updated!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editActivity.this, "Failed to update the journal.",Toast.LENGTH_SHORT).show();
            }
        });

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        switch(sp.getInt(appSettingsActivity.TEXTFONT, 0))   {

            case 0:
                title.setTypeface(Typeface.SANS_SERIF);
                input.setTypeface(Typeface.SANS_SERIF);
                break;

            case 1:
                title.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.aguafina_script));
                input.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.aguafina_script));
                break;

            case 2:
                title.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                input.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                break;

            case 3:
                title.setTypeface(Typeface.SERIF);
                input.setTypeface(Typeface.SERIF);
                break;

            case 4:
                title.setTypeface(Typeface.MONOSPACE);
                input.setTypeface(Typeface.MONOSPACE);
                break;

            case 5:
                title.setTypeface(Typeface.create("serif-monospace", Typeface.NORMAL));
                input.setTypeface(Typeface.create("serif-monospace", Typeface.NORMAL));
                break;

            case 6:
                title.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                input.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                break;

            case 7:
                title.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
                input.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
                break;
        }
    }
}