package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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


    }
}