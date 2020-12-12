package com.example.journalingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class appSettingsActivity extends AppCompatActivity {

    ConstraintLayout layoutm;
    RadioGroup backgroundChoice;
    RadioGroup fontChoice;

    Button colorPicker12;
    Button colorPicker22;
    Button colorPicker11;

    int colorUni = 0;
    int color1 = 0;
    int color2 = 0;

    int textFont = 0;
    int backgroundOption = 0;

    public static final String SHARED_PREFS = "appSettings";
    public static final String BACKGROUND_CHOICE = "bG";
    public static final String BACKGROUND_ONEONE = "bG11";
    public static final String BACKGROUND_ONETWO = "bG12";
    public static final String BACKGROUND_TWOTWO = "bG22";
    public static final String TEXTFONT = "tf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        layoutm = findViewById(R.id.appSettingsLayout);
        backgroundChoice = findViewById(R.id.backgroundChooseRadioGroup);
        fontChoice = findViewById(R.id.textFontRadioGroup);

        colorPicker11 = findViewById(R.id.backgroundColorPickerOneOne);
        colorPicker12 = findViewById(R.id.backgroundColorPickerOneTwo);
        colorPicker22 = findViewById(R.id.backgroundColorPickerTwoTwo);

        colorPicker11.setVisibility(View.INVISIBLE);
        colorPicker12.setVisibility(View.INVISIBLE);
        colorPicker22.setVisibility(View.INVISIBLE);

        backgroundChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int backgroundOptionn = radioGroup.getCheckedRadioButtonId();

                switch (backgroundOptionn)   {
                    case R.id.backgroundDefaultRadio:
                        backgroundOption = 0;
                        colorPicker11.setVisibility(View.INVISIBLE);
                        colorPicker12.setVisibility(View.INVISIBLE);
                        colorPicker22.setVisibility(View.INVISIBLE);
                        defaultChange();
                        break;

                    case R.id.backgroundFillRadio:
                        backgroundOption = 1;
                        colorPicker11.setVisibility(View.VISIBLE);
                        colorPicker12.setVisibility(View.INVISIBLE);
                        colorPicker22.setVisibility(View.INVISIBLE);
                        oneColorChange();
                        break;

                    case R.id.backgroundMixedRadio:
                        backgroundOption = 2;
                        colorPicker11.setVisibility(View.INVISIBLE);
                        colorPicker12.setVisibility(View.VISIBLE);
                        colorPicker22.setVisibility(View.VISIBLE);
                        twoColorChange();
                        break;

                    case R.id.backgroundAnimatedRadio:
                        backgroundOption = 3;
                        colorPicker11.setVisibility(View.INVISIBLE);
                        colorPicker12.setVisibility(View.INVISIBLE);
                        colorPicker22.setVisibility(View.INVISIBLE);
                        animatedChange();
                        break;
                }
            }
        });

        fontChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int textFontR = radioGroup.getCheckedRadioButtonId();

                switch(textFontR)   {
                    case R.id.fontfamilyRadioSS:
                        textFont = 0;
                        break;

                    case R.id.fontfamilyRadioAq:
                        textFont = 1;
                        break;

                    case R.id.fontfamilyRadioSSB:
                        textFont = 2;
                        break;

                    case R.id.fontfamilyRadioS:
                        textFont = 3;
                        break;

                    case R.id.fontfamilyRadioMn:
                        textFont = 4;
                        break;

                    case R.id.fontfamilyRadioSMn:
                        textFont = 5;
                        break;

                    case R.id.fontfamilyRadioCs:
                        textFont = 6;
                        break;

                    case R.id.fontfamilyRadioCr:
                        textFont = 7;
                        break;
                }
            }
        });

        loadSharedPreferences();

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

                color2 = color;

                GradientDrawable gd = new GradientDrawable();

                gd.setOrientation(GradientDrawable.Orientation.BL_TR);

                gd.setColors(new int[]  {color1, color2});

                layoutm.setBackground(gd);
            }
        });

        colorPicker.show();
    }

    public void colorOneTwo(View view) {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                color1 = color;

                twoColorChange();
            }
        });

        colorPicker.show();
    }

    public void colorOneOne(View view) {

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                colorUni = color;

                oneColorChange();
            }
        });

        colorPicker.show();
    }

    public void twoColorChange()    {

        GradientDrawable gd = new GradientDrawable();

        gd.setOrientation(GradientDrawable.Orientation.BL_TR);

        gd.setColors(new int[]  {color1, color2});

        layoutm.setBackground(gd);
    }

    public void oneColorChange()    {

        layoutm.setBackgroundColor(colorUni);
    }

    public void defaultChange()    {

        layoutm.setBackground(getDrawable(R.drawable.bglogin));
    }

    public void animatedChange()    {

        layoutm.setBackground(getDrawable(R.drawable.gradient_list));

        AnimationDrawable animationDrawable = (AnimationDrawable) layoutm.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public void goHome(){

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void saveExit(View view) {

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(BACKGROUND_CHOICE, backgroundOption);
        editor.putInt(TEXTFONT, textFont);
        editor.putInt(BACKGROUND_ONEONE, colorUni);
        editor.putInt(BACKGROUND_ONETWO, color1);
        editor.putInt(BACKGROUND_TWOTWO, color2);

        editor.apply();

        Toast.makeText(this, "Changes have been saved", Toast.LENGTH_SHORT).show();

        goHome();
    }

    public void loadSharedPreferences()  {

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        backgroundOption = sp.getInt(BACKGROUND_CHOICE, 0);
        textFont = sp.getInt(TEXTFONT, 0);
        colorUni = sp.getInt(BACKGROUND_ONEONE, 0);
        color1 = sp.getInt(BACKGROUND_ONETWO, 0);
        color2 = sp.getInt(BACKGROUND_TWOTWO, 0);


        switch(backgroundOption)    {

            case 0:
                ((RadioButton) findViewById(R.id.backgroundDefaultRadio)).setChecked(true);
                defaultChange();
                break;

            case 1:
                ((RadioButton) findViewById(R.id.backgroundFillRadio)).setChecked(true);
                oneColorChange();
                break;

            case 2:
                ((RadioButton) findViewById(R.id.backgroundMixedRadio)).setChecked(true);
                twoColorChange();
                break;

            case 3:
                ((RadioButton) findViewById(R.id.backgroundAnimatedRadio)).setChecked(true);
                animatedChange();
                break;
        }

        switch(textFont)   {

            case 0:
                ((RadioButton) findViewById(R.id.fontfamilyRadioSS)).setChecked(true);
                break;

            case 1:
                ((RadioButton) findViewById(R.id.fontfamilyRadioAq)).setChecked(true);
                break;

            case 2:
                ((RadioButton) findViewById(R.id.fontfamilyRadioSSB)).setChecked(true);
                break;

            case 3:
                ((RadioButton) findViewById(R.id.fontfamilyRadioS)).setChecked(true);
                break;

            case 4:
                ((RadioButton) findViewById(R.id.fontfamilyRadioMn)).setChecked(true);
                break;

            case 5:
                ((RadioButton) findViewById(R.id.fontfamilyRadioSMn)).setChecked(true);
                break;

            case 6:
                ((RadioButton) findViewById(R.id.fontfamilyRadioCs)).setChecked(true);
                break;

            case 7:
                ((RadioButton) findViewById(R.id.fontfamilyRadioCr)).setChecked(true);
                break;
        }

    }
}