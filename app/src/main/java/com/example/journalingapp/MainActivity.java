package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button add;
    RecyclerView recyclerView;
    recyclerviewadapter rva;
    ArrayList<Entry> entries;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Profile:
                showProfile();
                return true;
            case R.id.logout:
                logout();
                return true;
            case R.id.Setting:
                setting();
                return true;
            default:
                return false;
        }

    }

    public void showProfile() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    //needs to be changed to the setting activity.
    public void setting() {
        startActivity(new Intent(getApplicationContext(), appSettingsActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        recyclerView = findViewById(R.id.entryList);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addEntry.class));
                finish();
            }
        });

        //create temporary Entry item, grab content from database, put content in entry item, but entry item in ArrayList
        //use ArrayList with RecyclerViewAdapter to load RecyclerView with Entry items

        getData();
        loadSharedPreferences();
    }


    public void getData() {

        final ArrayList<Entry> entryList = new ArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference entryReference = db.collection("journals");
        Query entryQuery = entryReference.whereEqualTo("UserID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        entryQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    Log.d("Success", "Task was successful");
                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        Entry entry = doc.toObject(Entry.class);
                        entryList.add(entry);
                    }

                    Log.d("EntryList", entryList.toString());
                    //tell rva that dataset is changed (maybe)
                    getTheResult(entryList);
                } else {
                    Log.e("Failed", "Task has failed");
                    Toast.makeText(getApplicationContext(), "Task failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getTheResult(ArrayList<Entry> pass) {
        entries = new ArrayList<>(pass);
        System.out.println(("Testing the outcome ===============================" + entries));

        rva = new recyclerviewadapter(entries, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rva);

        System.out.println("Setting adapter");

        rva.setOnItemClickListener(new recyclerviewadapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                //open edit activity
            }

            @Override
            public void onDeleteClick(int position) {
                entries.remove(position);
                rva.notifyItemRemoved(position);
                //remove item from cloud firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("journals").document("QUzL19OyD4ioUA5WpJdu")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Document deleted successfully!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to  deleted the document!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    public void loadSharedPreferences() {

        SharedPreferences sp = getSharedPreferences(appSettingsActivity.SHARED_PREFS, MODE_PRIVATE);

        ConstraintLayout layoutm = findViewById(R.id.mainActivityLayout);

        switch (sp.getInt(appSettingsActivity.BACKGROUND_CHOICE, 0)) {

            case 0:
                layoutm.setBackground(getDrawable(R.drawable.bglogin));
                break;

            case 1:
                layoutm.setBackgroundColor(sp.getInt(appSettingsActivity.BACKGROUND_ONEONE, 0));
                break;

            case 2:
                GradientDrawable gd = new GradientDrawable();

                gd.setOrientation(GradientDrawable.Orientation.BL_TR);

                gd.setColors(new int[]{sp.getInt(appSettingsActivity.BACKGROUND_ONETWO, 0), sp.getInt(appSettingsActivity.BACKGROUND_TWOTWO, 0)});

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