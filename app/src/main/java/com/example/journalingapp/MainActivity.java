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

import com.google.firebase.auth.FirebaseAuth;

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

        ArrayList<Entry> entryList = new ArrayList();

   /*     if() { //if there is an entry in the database
            do {

                String title = ""; //grab from firebase
                Date date = null;
                String content = "";
                String name = ""; //get logged in user's username from firebase;

                Entry tempEntry = new Entry(title, content, date, name);
                entryList.add(tempEntry);

            } while (); //while there is still another entry in the table
        }
        else{
            Log.e("Empty", "No entries found.");
        } */

        return entryList;
    }


}