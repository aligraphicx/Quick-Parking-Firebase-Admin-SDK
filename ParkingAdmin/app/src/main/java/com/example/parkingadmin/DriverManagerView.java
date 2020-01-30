package com.example.parkingadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DriverManagerView extends AppCompatActivity {


    private static final String TAG = "DriverManagerView";;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_manager_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        loadUseresDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DriverManagerView.this,NewDriver.class));
            }
        });
    }


    private void loadUseresDatabase(){


        final ProgressDialog progressDialog=new ProgressDialog(DriverManagerView.this);
        progressDialog.setTitle("Loading . . .");
        progressDialog.setMessage("Getting Data from server . . .");



        DatabaseReference databaseReference=FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE).getReference("Drivers Personal Information");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressDialog.show();
                   }
               });

                final List<DriverType> driverRecord=new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()){

                    Log.d(TAG, "onDataChange: "+data.getValue(DriverType.class).getLicenseNumber());
                    DriverType d=data.getValue(DriverType.class);
                    driverRecord.add(d);
                }

                Log.d(TAG, "onDataChange: "+driverRecord.size());

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         RecyclerView recyclerView=findViewById(R.id.recyclerView);
                         recyclerView.setLayoutManager(new LinearLayoutManager(DriverManagerView.this));
                         DriversViewAdaptor driversViewAdaptor=new DriversViewAdaptor(DriverManagerView.this,driverRecord);
                         driversViewAdaptor.notifyDataSetChanged();
                         recyclerView.setAdapter(driversViewAdaptor);
                         progressDialog.dismiss();
                     }
                 });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }



}
