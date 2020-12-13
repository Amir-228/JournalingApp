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

        title = findViewById(R.id.titleInput2);
        input = findViewById(R.id.entryInputText2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        getData();

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

                    String titleText = entryList.get(0).getTitle();
                    title.setText(titleText);

                    String contentText = entryList.get(0).getContent();
                    input.setText(contentText);

                    System.out.println("==================== hereeeeeeeeeeeeeeee!");

                } else {
                    Log.e("Failed", "Task has failed");
                    Toast.makeText(getApplicationContext(), "Task failed", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                        Toast.makeText(editActivity.this, "Journal added to the Database.",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editActivity.this, "Failed to upload to the Database.",Toast.LENGTH_SHORT).show();
            }
        });

        startActivity(new Intent(getApplicationContext(), addEntry.class));
        finish();

    }
}