    package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    public void setting(){
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

        ArrayList<Entry> entries = getData();

        Log.d("Array", entries.isEmpty() + "");

        rva = new recyclerviewadapter(entries, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rva);

    }


    public ArrayList<Entry> getData(){

        final ArrayList<Entry> entryList = new ArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference entryReference = db.collection("journals");
        Query entryQuery = entryReference.whereEqualTo("UserID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        entryQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    Log.d("Success", "Task was successful");


                    for(QueryDocumentSnapshot doc: task.getResult()){

                        Entry entry = doc.toObject(Entry.class);
                        entryList.add(entry);
                    }

                    Log.d("EntryList", entryList.toString());

                    //tell rva that dataset is changed (maybe)

                }
                else{
                    Log.e("Failed", "Task has failed");
                    Toast.makeText(getApplicationContext(), "Task failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        return entryList;
    }
}