package com.dut.blablacarfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    public static final String LANGUAGE_ENGLISH = "eng";
    public static final String LANGUAGE_FRENCH = "fr";
    public static final String INTENT_RESULT = "settings_result";

    public static String language = "fr";
    public static boolean isDistanceMeters = false;
    public static boolean isDarkMode = true;

    Button btSave;
    RadioGroup rgLanguage, rgDistance;
    Switch darkMode;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rgLanguage = findViewById(R.id.rg_language);
        rgDistance = findViewById(R.id.rg_distance);
        darkMode = findViewById(R.id.switch_dark_mode);

        setParameters();

        btSave = findViewById(R.id.bt_save);
        btSave.setOnClickListener(view -> {
            saveSettings();
            Intent intentResult = new Intent();
            intentResult.putExtra(INTENT_RESULT, "changed");
            setResult(RESULT_OK, intentResult);
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void saveSettings(){
        switch (rgLanguage.getCheckedRadioButtonId()){
            case R.id.rb_english:
                language = LANGUAGE_ENGLISH;
                break;
            case R.id.rb_french:
                language = LANGUAGE_FRENCH;
                break;
        };
        isDistanceMeters = (rgDistance.getCheckedRadioButtonId() == R.id.rb_meters);
        isDarkMode = darkMode.isChecked();
        setLanguage(language);
    }

    private void setLanguage(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setParameters(){
        switch (language){
            case "eng":
                rgLanguage.check(R.id.rb_english);
                break;
            case "fr":
                rgLanguage.check(R.id.rb_french);
                break;
        }

        if(isDistanceMeters){
            rgDistance.check(R.id.rb_meters);
        } else {
            rgDistance.check(R.id.rb_kilometers);
        }

        darkMode.setChecked(isDarkMode);
        if(isDarkMode){
            ConstraintLayout layout = findViewById(R.id.cl_settings);
            layout.setBackgroundColor(getColor(R.color.black));

            TextView tv = findViewById(R.id.tv_settings);
            setWithDarkMode(tv);
            tv = findViewById(R.id.tv_language);
            setWithDarkMode(tv);
            tv = findViewById(R.id.tv_distance_settings);
            setWithDarkMode(tv);
            tv = findViewById(R.id.tv_dark_mode);
            setWithDarkMode(tv);

            RadioButton rb = findViewById(R.id.rb_french);
            setWithDarkMode(rb);
            rb = findViewById(R.id.rb_english);
            setWithDarkMode(rb);
            rb = findViewById(R.id.rb_meters);
            setWithDarkMode(rb);
            rb = findViewById(R.id.rb_kilometers);
            setWithDarkMode(rb);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWithDarkMode(TextView view){
        if(Settings.isDarkMode){
            view.setTextColor(view.getContext().getColor(R.color.white));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWithDarkMode(RadioButton view){
        if(Settings.isDarkMode){
            view.setTextColor(view.getContext().getColor(R.color.white));
        }
    }
}