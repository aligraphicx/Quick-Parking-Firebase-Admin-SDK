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

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BugReportCenter extends AppCompatActivity {
    private EditText problemName;
    private EditText problemDiscription;
    private Button submitBtn;

    private static final String TAG = "BugReportCenter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report_center);

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

                    AlertDialog submitAlert= new AlertDialog.Builder(BugReportCenter.this)
                            .setTitle("Report Submit")
                            .setMessage("Do you want to submit this Bug report")
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    final ProgressDialog progressDialog=new ProgressDialog(BugReportCenter.this);
                                    progressDialog.setMessage("Wait for server reponse");
                                    progressDialog.show();
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_HR_DATABASE).getReference("Parking Admin Bug Reports");
                                    databaseReference.child("This Parking ID").child(problem).setValue(problemDis, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                    AlertDialog successAlert=new AlertDialog.Builder(BugReportCenter.this)
                                                            .setCancelable(false)
                                                            .setMessage("Your Bug Report Submit Sccessfuly our team solve it in next update")
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
