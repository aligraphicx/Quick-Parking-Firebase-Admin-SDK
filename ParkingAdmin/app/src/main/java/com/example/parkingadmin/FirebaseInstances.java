package com.example.parkingadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FirebaseInstances extends AppCompatActivity {

    private static final String TAG = "FirebaseInstances";


    public static FirebaseApp FIREBASE_ParkingDriver_AUTH;
    public static FirebaseApp FIREBASE_PARKINGUSER_AUTH;
    public static FirebaseApp FIREBASE_PARKINGUSER_DATABASE;
    public static FirebaseApp FIREBASE_ParkingDriver_DATABASE;
    public static FirebaseApp FIREBASE_ParkingDriver_HR_DATABASE;
    public static FirebaseApp FIREBASE_ParkingDriver_BUCKET;
    public static FirebaseApp FIREBASE_PARKINGADMIN_AUTH;
    public static FirebaseApp FIREBASE_PARKINGADMIN_DATABASE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_instances);




        if(isNetworkConnected()){

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNetworkWorking()){

                            Log.d(TAG, "run: working");

                            if(firebaseParkingDriverAuth()&&firebaseParkingDriverDatabase()&&firebaseParkingDriverHRDatabase()&&firebaseParkingDriverBucket()
                            &&firebaseParkingDriverAdminDatabase()&&firebaseParkingDriverAdminAuth())
                            {

                                        startActivity(new Intent(FirebaseInstances.this,MainMenu.class));
                                        finish();

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialoge("Alert","Something went to wrong try again later");

                                    }
                                });
                            }
                        }else {

                            Log.d(TAG, "run:  not working");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlertDialoge("Network","Your internet not working correctly");

                                }
                            });

                        }

                    }
                });
                thread.start();



        }else {

            showAlertDialoge("Network","Please connect network and try again later");
        }





    }


    public  void showAlertDialoge(String title,String mesg){


        AlertDialog alertDialog=new AlertDialog.Builder(FirebaseInstances.this).setCancelable(false).setCancelable(false)
                .setTitle(title)
                .setMessage(mesg)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();

        alertDialog.show();
    }


    public  boolean isNetworkConnected(){

        boolean n=false;
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo network=connectivityManager.getActiveNetworkInfo();

        if(network!=null&&network.isConnected()){

            n=true;
        }

        return n;
    }


    public boolean isNetworkWorking(){

        boolean working=false;
        Socket socket=new Socket();
        try {
            socket.connect(new InetSocketAddress("www.google.com",80),1000);
            working=true;
        } catch (IOException e) {
            Log.e(TAG, "isNetworkWorking: "+e.getMessage());
            working=false;

        }

        return working;
    }
    public boolean firebaseParkingDriverAuth(){

        boolean co=false;
        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quick_parking_driver);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build();
            Log.d(TAG, "onCreate: " + "Parking Driver Auth initialized");
            try {
                FIREBASE_ParkingDriver_AUTH= FirebaseApp.initializeApp(options,"Parking Driver Auth");
            }catch (Exception e){
                FIREBASE_ParkingDriver_AUTH= FirebaseApp.getInstance("Parking Driver Auth");
            }
            co=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            co=false;
        }

        return co;
    }







    public  boolean firebaseParkingDriverDatabase(){

        boolean connect=false;

        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quick_parking_driver);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setDatabaseUrl("https://quick-parking-driver.firebaseio.com/")
                    .build();

            try{
                FIREBASE_ParkingDriver_DATABASE= FirebaseApp.initializeApp(options,"Parking Driver Database");
            }catch (Exception e){
                FIREBASE_ParkingDriver_DATABASE=FirebaseApp.getInstance("Parking Driver Database");
            }
            Log.d(TAG, "onCreate: " + "Parking Driver Database initalized");

            connect=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            connect=false;
        }

        return connect;
    }
    public  boolean firebaseParkingDriverHRDatabase(){

        boolean connect=false;

        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quick_parking_hr);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setDatabaseUrl("https://quick-parking-hr.firebaseio.com/")
                    .build();

            try{
                FIREBASE_ParkingDriver_HR_DATABASE= FirebaseApp.initializeApp(options,"Parking Driver HR Database");
            }catch (Exception e){
                FIREBASE_ParkingDriver_HR_DATABASE=FirebaseApp.getInstance("Parking Driver HR Database");
            }
            Log.d(TAG, "onCreate: " + "Parking Driver HR Database initalized");

            connect=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            connect=false;
        }

        return connect;
    }
    public  boolean firebaseParkingDriverBucket(){

        boolean connect=false;

        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quick_parking_driver);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setStorageBucket("https://quick-parking-driver.appspot.com/")
                    .build();

            try{
                FIREBASE_ParkingDriver_BUCKET= FirebaseApp.initializeApp(options,"Parking Driver Bucket");
            }catch (Exception e){
                FIREBASE_ParkingDriver_BUCKET=FirebaseApp.getInstance("Parking Driver Bucket");
            }
            Log.d(TAG, "onCreate: " + "Parking Driver Bucket initalized");

            connect=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            connect=false;
        }

        return connect;
    }


    public boolean firebaseParkingUserAuth(){

        boolean co=false;
        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quickparking_user);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build();
            Log.d(TAG, "onCreate: " + "Parking User Auth initialized");
            try {
                FIREBASE_PARKINGUSER_AUTH= FirebaseApp.initializeApp(options,"Parking User Auth");
            }catch (Exception e){
                FIREBASE_PARKINGUSER_AUTH= FirebaseApp.getInstance("Parking User Auth");
            }
            co=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            co=false;
        }

        return co;
    }
    public  boolean firebaseParkingUserDatabase(){

        boolean connect=false;

        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.quickparking_user);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setDatabaseUrl("https://quickparking-c5834.firebaseio.com/")
                    .build();
            try{
                FIREBASE_PARKINGUSER_DATABASE= FirebaseApp.initializeApp(options,"Parking User Database");
            }catch (Exception e){
                FIREBASE_PARKINGUSER_DATABASE=FirebaseApp.getInstance("Parking User Database");
            }
            Log.d(TAG, "onCreate: " + "Parking User initalized");

            connect=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            connect=false;
        }

        return connect;
    }
    public boolean firebaseParkingDriverAdminAuth(){

        boolean co=false;
        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.parking_admin_services);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build();
            Log.d(TAG, "onCreate: " + "Parking Driver Admin Auth initialized");
            try {
                FIREBASE_PARKINGADMIN_AUTH= FirebaseApp.initializeApp(options,"Parking Driver Admin Auth");
            }catch (Exception e){
                FIREBASE_PARKINGADMIN_AUTH= FirebaseApp.getInstance("Parking Driver Admin Auth");
            }
            co=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            co=false;
        }

        return co;
    }


    public  boolean firebaseParkingDriverAdminDatabase(){

        boolean connect=false;

        InputStream stream = getApplicationContext().getResources().openRawResource(R.raw.parking_admin_services);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setDatabaseUrl("https://fir-testingapplication-8d620.firebaseio.com/")
                    .build();

            try{
                FIREBASE_PARKINGADMIN_DATABASE= FirebaseApp.initializeApp(options,"Parking Driver Admin Database");
            }catch (Exception e){
                FIREBASE_PARKINGADMIN_DATABASE=FirebaseApp.getInstance("Parking Driver Admin Database");
            }
            Log.d(TAG, "onCreate: " + "Parking Driver Database initalized");

            connect=true;

        } catch (IOException ex) {

            Log.d(TAG, "onCreate: " + ex.getMessage());
            connect=false;
        }

        return connect;
    }

}
