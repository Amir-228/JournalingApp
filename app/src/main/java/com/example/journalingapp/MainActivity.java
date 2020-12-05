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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        final ArrayList<Entry> entries = getData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rva = new recyclerviewadapter(entries, this);
        recyclerView.setAdapter(rva);

    }


    public ArrayList getData(){

        final ArrayList<Entry> entryList = new ArrayList();

        DocumentReference df = FirebaseFirestore.getInstance().collection("journals").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {         //this part doesn't work ^^^ | that doesn't retrieve the document
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){

                        String title = document.getString("Title");
                        String content = document.getString("Content");
                        String name = document.getString("UserID");


                        Entry entryObj = new Entry(title, content, name);

                        entryList.add(entryObj);
                    }
                    else{
                        Log.wtf("wtf", "" + document);
                        Log.e("None", "No such document exists.");
                    }
                }
                else{
                    Log.e("Failed", "" + task.getException());
                }
            }
        });

        return entryList;
    }
}