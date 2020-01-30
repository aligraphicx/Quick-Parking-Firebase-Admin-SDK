package com.example.parkingadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private CardView driverManager;
    private CardView parkingManager;
    private CardView history;
    private CardView feedback;
    private CardView support;
    private CardView setting;
    private CardView logout;
    private CardView submitVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }
        initObjects();

    }

    private void initObjects(){

        driverManager=findViewById(R.id.driverManager);
        feedback=findViewById(R.id.feedback);
        support=findViewById(R.id.support);
        setting=findViewById(R.id.setting);
        logout=findViewById(R.id.logout);
        submitVehicle=findViewById(R.id.submitVehicle);
        driverManager.setOnClickListener(this);
        feedback.setOnClickListener(this);
        support.setOnClickListener(this);
        setting.setOnClickListener(this);
        logout.setOnClickListener(this);
        submitVehicle.setOnClickListener(this);


    }

    private  void logOut(){

        Intent intent=getPackageManager().getLaunchIntentForPackage("com.example.adminloginapp");
        startActivity(intent);
        finish();

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");





                View view= LayoutInflater.from(MainMenu.this).inflate(R.layout.vehicle_submit,null);
                AlertDialog keyDialoge= getAlertDialoge("Search Order",view);
                keyDialoge.show();
                final EditText searchKey=view.findViewById(R.id.orderKey);
                Button searchBtn=view.findViewById(R.id.keySearchBtn);

                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!searchKey.getText().toString().isEmpty()){

                            searchKey(searchKey.getText().toString(),result.getContents());

                        }else{

                            Toast.makeText(MainMenu.this, "Enter a Key", Toast.LENGTH_SHORT).show();
                        }
                    }
                });









            }
            }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.driverManager:

                Toast.makeText(this, "Driver Manager", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainMenu.this,DriverManager.class));

                break;
            case R.id.submitVehicle:
                qrCodeScanner();
                break;

            case R.id.feedback:
                Toast.makeText(this, "Feedback Manager", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainMenu.this,BugReportCenter.class));
                break;
            case R.id.support:
                Toast.makeText(this, "support Manager", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainMenu.this,SupportCenter.class));
                break;
            case R.id.setting:
                Toast.makeText(this, "setting Manager", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
                break;
            case R.id.logout:
                Toast.makeText(this, "logout Manager", Toast.LENGTH_SHORT).show();
                logOut();
                break;

        }


    }





    public AlertDialog getAlertDialoge(String title,View view){



        return  new AlertDialog.Builder(MainMenu.this).setTitle("Key Varification").setTitle(title).setCancelable(false)
                .setNegativeButton("Cancel",null)
                .setView(view).create();


    }




    public void searchKey(final String k,String uid){

        Toast.makeText(this, "Enter in Function", Toast.LENGTH_SHORT).show();
       DatabaseReference d= FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE).getReference("Drivers Personal Information");

        final ProgressDialog progress=new ProgressDialog(MainMenu.this);
        progress.show();

       d.child(uid).child("Pending Order").child(k).child("Vehicle Information").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(final DataSnapshot snapshot) {


               if(snapshot.exists()){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           final VehicalInformation vInformation;
                           vInformation=snapshot.getValue(VehicalInformation.class);
                           snapshot.getRef().getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   final LiveOrder orderDetail;
                                   orderDetail=dataSnapshot.getValue(LiveOrder.class);

                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run()
                                       {

                                           getVehicleView(vInformation,orderDetail,k).show();
                                           progress.dismiss();

                                       }
                                   });

                               }

                               @Override
                               public void onCancelled(DatabaseError error) {

                               }
                           });


                       }
                   });

               }else{
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(MainMenu.this, "Path not found", Toast.LENGTH_SHORT).show();
                           progress.dismiss();
                       }
                   });
               }


           }

           @Override
           public void onCancelled(DatabaseError error) {

           }
       });

    }
    public void qrCodeScanner(){

        IntentIntegrator integrator = new IntentIntegrator(MainMenu.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan Driver QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    private AlertDialog getVehicleView(final VehicalInformation ve, final LiveOrder liveOrder, final String key){

        View vLayout=LayoutInflater.from(MainMenu.this).inflate(R.layout.vehicle_view,null);

        TextView vehicleName=vLayout.findViewById(R.id.vehicleName);
        TextView vehicleNum=vLayout.findViewById(R.id.vehicleNumber);
        TextView ownerName=vLayout.findViewById(R.id.vehicleOwnerName);
        TextView ownerEmail=vLayout.findViewById(R.id.vehicleOwnerEmail);
        TextView ownerNumber=vLayout.findViewById(R.id.vehicleOwnerPhone);
        TextView ownerCnic=vLayout.findViewById(R.id.vehicleOwnerID);
        TextView ownerAddress=vLayout.findViewById(R.id.vehvileOwnerAddress);

        Toast.makeText(this, ve.getOwnerName(), Toast.LENGTH_SHORT).show();
        Button confirmBtn=vLayout.findViewById(R.id.vehicleConfirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_PARKINGADMIN_DATABASE).getReference("Submit Orders")
                        .child(liveOrder.getUserUid()).child(key).setValue(liveOrder, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_PARKINGADMIN_DATABASE).getReference("Submit Orders")
                                .child(liveOrder.getUserUid()).child(key).child("Vehicle Information").setValue(ve,null);
                        FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE).getReference("Drivers Personal Information")
                                .child(liveOrder.getDriverUID()).child("Pending Order").child(key).removeValue(null);
                    }
                });


            }
        });


        vehicleName.setText(ve.getVehicalCompany());
        vehicleNum.setText(ve.getVehicalNumber());
        ownerEmail.setText(ve.getOwnerEmail());
        ownerName.setText(ve.getOwnerName());
        ownerNumber.setText(ve.getOwnerMobile());
        ownerCnic.setText(ve.getIdNumber());
        ownerAddress.setText(ve.getOwnerAddress());


        return new AlertDialog.Builder(MainMenu.this).setView(vLayout).create();



    }

}
