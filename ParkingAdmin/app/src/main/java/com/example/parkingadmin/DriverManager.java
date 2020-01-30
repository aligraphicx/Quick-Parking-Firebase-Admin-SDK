package com.example.parkingadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DriverManager extends AppCompatActivity implements View.OnClickListener{

    private Button addDriver;
    private Button viewAllDriver;
    private static final String TAG = "DriverManager";
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_manager);
        addDriver=findViewById(R.id.addDriver);
        addDriver.setOnClickListener(this);
        viewAllDriver=findViewById(R.id.allDrivers);
        viewAllDriver.setOnClickListener(this);
        auth = FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH);



    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.addDriver:

                startActivity(new Intent(DriverManager.this,NewDriver.class));
                break;

            case R.id.allDrivers:
                startActivity(new Intent(DriverManager.this,DriverManagerView.class));
                break;

            case R.id.support:
                startActivity(new Intent(DriverManager.this,SupportCenter.class));
                break;
        }
    }
}
