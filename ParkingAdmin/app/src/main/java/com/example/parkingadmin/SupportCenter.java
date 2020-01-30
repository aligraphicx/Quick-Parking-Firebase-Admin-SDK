package com.example.parkingadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SupportCenter extends AppCompatActivity {

    private EditText problemName;
    private EditText problemDiscription;
    private Button submitBtn;

    private static final String TAG = "SupportCenter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_center);

        if(getSupportActionBar().isShowing()){

            getSupportActionBar().hide();
        }

        problemName=findViewById(R.id.support_center_problemName);
        problemDiscription=findViewById(R.id.support_center_problemDiscription);
        submitBtn=findViewById(R.id.support_center_submit);




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String problem=problemName.getText().toString();
               final String problemDis=problemDiscription.getText().toString();

                if(problem.isEmpty()){


                }else if(problemDis.isEmpty()){

                }else{

                    AlertDialog submitAlert= new AlertDialog.Builder(SupportCenter.this)
                            .setTitle("Report Submit")
                            .setMessage("Do you want to submit this report")
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    final ProgressDialog progressDialog=new ProgressDialog(SupportCenter.this);
                                    progressDialog.setMessage("Wait for server reponse");
                                    progressDialog.show();
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_HR_DATABASE).getReference("Parking Admin Support");
                                    databaseReference.child("This Parking ID").child(problem).setValue(problemDis, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                    AlertDialog successAlert=new AlertDialog.Builder(SupportCenter.this)
                                                            .setCancelable(false)
                                                            .setMessage("Your Report Submit Sccessfuly our team contact you with in 3 hours")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    finish();
                                                                }
                                                            }).create();
                                                    successAlert.show();
                                                    Log.d(TAG, "onComplete: Report Saved");
                                                }
                                            });

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setCancelable(false)
                            .create();
                    submitAlert.show();
                }
            }
        });

    }
}
