package com.example.parkingadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.cloud.storage.Acl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverView extends AppCompatActivity implements View.OnClickListener{

    private ImageView cover;
    private TextView name;
    private TextView cnic;
    private TextView dateofbirth;
    private TextView licenceType;
    private TextView gender;
    private TextView address;
    private TextView phoneNumber;
    private TextView email;
    private TextView licenseNumber;


    private static final String TAG = "DriverView";



    private Button blockBtn;
    private Button deleteBtn;
    private Button sendNotification;




    private DriverType recorde;
    private UserRecord currentDriver;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view);
        recorde=(DriverType) getIntent().getParcelableExtra("recorde");
        cover=findViewById(R.id.driverView_profile);
        name=findViewById(R.id.driverView_name);
        email=findViewById(R.id.driverView_email);
        cnic=findViewById(R.id.driverView_cnic );
        dateofbirth=findViewById(R.id.driverView_dateofbirth);
        licenceType=findViewById(R.id.driverView_licenseType);
        gender=findViewById(R.id.driverView_gender);
        phoneNumber=findViewById(R.id.driverView_phone);
        address=findViewById(R.id.driverView_address);
        licenseNumber=findViewById(R.id.driverView_licenseNumber);
        blockBtn=findViewById(R.id.driverView_block);
        deleteBtn=findViewById(R.id.driverView_delete);
        sendNotification=findViewById(R.id.driverView_sendNotification);
        progressDialog=new ProgressDialog(DriverView.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("processing please wait ");
        progressDialog.show();

        blockBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        sendNotification.setOnClickListener(this);
     //   Log.d(TAG, "onCreate: "+recorde.getLicenseNumber());

        Glide.with(this).load(recorde.getProfileLink()).override(512,512).placeholder(R.drawable.profile).into(cover);
        name.setText(recorde.getFirstName()+" "+recorde.getLastName());
        email.setText(recorde.getEmail());
        cnic.setText(recorde.getCNIC());
        dateofbirth.setText(recorde.getDateofBirth());
        licenceType.setText(recorde.getLicenseType());
        gender.setText(recorde.getGender());
        address.setText(recorde.getAddress());
        phoneNumber.setText(recorde.getPhoneNumber());
        licenseNumber.setText(recorde.getLicenseNumber());
       // Log.d(TAG, "onCreate: "+recorde.getUid());



        Thread getUserThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currentDriver =FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH).getUserByEmail(recorde.getEmail());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: "+ currentDriver.getEmail());

                            if(currentDriver.isDisabled()){
                                blockBtn.setText("Unblock");
                            }else{
                                blockBtn.setText("Block");

                            }
                            progressDialog.dismiss();
                        }
                    });
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                }
            }
        });

        getUserThread.start();



    }

    @Override
    public void onClick(View v) {



        switch (v.getId())
        {

            case R.id.driverView_block:

                if(currentDriver.isDisabled()){
                    blockUnblockDriver(false);
                }else{
                    blockUnblockDriver(true);
                }

                break;

            case R.id.driverView_sendNotification:

                sendNotification();
                break;
        }
    }


    private void blockUnblockDriver(final boolean action){

        Thread updateThread=new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();

                    }
                });
                UserRecord.UpdateRequest updateRequest=new UserRecord.UpdateRequest(recorde.getUid()).setDisabled(action);

                try {
                    FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH).updateUser(updateRequest);
                    currentDriver=FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH).getUserByEmail(recorde.getEmail());
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                Log.d(TAG, "run: Success");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(currentDriver.isDisabled()){
                            blockBtn.setText("Unblock");
                        }else{
                            blockBtn.setText("Block");

                        }
                    }
                });


            }
        });
        updateThread.start();
    }


    private void sendNotification(){

        final AlertDialog alertDialog=new AlertDialog.Builder(DriverView.this).setTitle("Send  Notification to "+recorde.getFirstName())
                .setView(R.layout.notification_layout).create();

        alertDialog.show();


        Button sendBtn=alertDialog.findViewById(R.id.notification_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText notificationName= alertDialog.findViewById(R.id.notification_name);
                final EditText notificationData= alertDialog.findViewById(R.id.notification_write);


                if(notificationName.getText().toString().isEmpty()){
                    notificationName.setError("Wrong Notification Title");
                    notificationName.requestFocus();
                }else if(notificationData.getText().toString().isEmpty()){

                    notificationData.setError("Write Something is here");
                    notificationData.requestFocus();
                }else {
                    progressDialog.show();
                    Thread sendNotificationThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Notification notification=new Notification(notificationName.getText().toString(),notificationData.getText().toString());
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE)
                                    .getReference("Notification");
                            databaseReference.child("Admin Name Here").setValue(notification, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError error, DatabaseReference ref) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            showAlertDialoge("Notification","Notification Send Successfully "+recorde.getFirstName());
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    });
                    sendNotificationThread.start();
                }


            }
        });


    }

    public  void showAlertDialoge(String title,String mesg){


        androidx.appcompat.app.AlertDialog alertDialog=new androidx.appcompat.app.AlertDialog.Builder(DriverView.this).setCancelable(false).setCancelable(false)
                .setTitle(title)
                .setMessage(mesg)
                .setPositiveButton("OK", null)
                .create();

        alertDialog.show();
    }


    public void deleteUser(final String email){

        Thread deleteUserThread=new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth driverAuth=FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH);
                try {
                    driverAuth.deleteUser(recorde.getUid());
                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                }
                DatabaseReference databaseReference=FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE).getReference("Drivers Personal Information");
                //databaseReference
            }
        });

    }
}
