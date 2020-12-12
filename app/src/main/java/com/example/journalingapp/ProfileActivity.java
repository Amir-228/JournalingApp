package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ListView listView;
    StorageReference storageReference;
    ImageButton imageButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profilemenu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.home:
                goToHome();
                return true;
            default:
                return false;
        }

    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void goToHome() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        listView = findViewById(R.id.listView);
        imageButton = findViewById(R.id.imageButton);
        final ArrayList<String> info = new ArrayList<String>();

        if (fAuth.getCurrentUser() != null) {
            DocumentReference df = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String fName = documentSnapshot.getString("fName");
                    String lName = documentSnapshot.getString("lName");
                    String userName = documentSnapshot.getString("userN");
                    info.add(fName);
                    info.add(lName);
                    info.add(userName);
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, info);
                    listView.setAdapter(arrayAdapter);
                    /*
                    //saves the image for next time *** NOT WORKING...
                    if (user.getPhotoUrl() != null) {
                        Glide.with(ProfileActivity.this)
                                .load(user.getPhotoUrl())
                                .into(imageButton);
                        Toast.makeText(ProfileActivity.this,"Did it Again!",Toast.LENGTH_SHORT).show();
                    }
                     */
                }
            });
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        loadSharedPreferences();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                imageButton.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child("Profiles")
                .child(user + ".jpeg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Image Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadSharedPreferences() {

        SharedPreferences sp = getSharedPreferences(appSettingsActivity.SHARED_PREFS, MODE_PRIVATE);

        ConstraintLayout layoutm = findViewById(R.id.profileEditLayout);

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