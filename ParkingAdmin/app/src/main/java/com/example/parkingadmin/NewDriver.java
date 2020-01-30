package com.example.parkingadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.cloud.firestore.v1.FirestoreAdminSettings;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.cloud.firestore.v1.FirestoreSettings;
import com.google.cloud.storage.Acl;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.Index;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Pattern;

public class NewDriver extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName;
    private EditText lastName;
    private EditText CNIC;
    private EditText licenseNumber;
    private EditText dateofBirth;
    private Spinner  licenseTypeSpinner;
    private Spinner genderSpinner;
    private EditText address;
    private EditText email;
    private EditText phoneNumber;
    private Button submitDriver;

    DatabaseReference database;
    private static final String TAG = "NewDriver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_driver);
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }

        initObjects();
        licenseSpinnerInit();
        genderSpinnerInit();











    }


    private void initObjects(){
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        CNIC=findViewById(R.id.CNIC);
        licenseNumber=findViewById(R.id.license_number);
        dateofBirth=findViewById(R.id.date_birth);
        dateofBirth.setOnClickListener(this);
        address=findViewById(R.id.address);
        phoneNumber=findViewById(R.id.phoneNumber);
        email=findViewById(R.id.email);
        submitDriver=findViewById(R.id.createNewDriver);
        submitDriver.setOnClickListener(this);

    }
    private void licenseSpinnerInit(){


        licenseTypeSpinner = (Spinner) findViewById(R.id.license_type);
        ArrayAdapter<CharSequence> license_adapter = ArrayAdapter.createFromResource(this,
                R.array.license_type, android.R.layout.simple_spinner_item);
        license_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        licenseTypeSpinner.setAdapter(license_adapter);

    }

    private void genderSpinnerInit(){
        genderSpinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);



    }

    private boolean driverDataValidation(DriverType data){

        boolean vali=false;

        if(data.getFirstName().length()<2){
            firstName.setError("Name is too short");
        }else if(data.getLastName().length()<2){
            lastName.setError("Name is too short");
        }else if(data.getCNIC().length()!=13){
            CNIC.setError("Incorrect id card number");
        }else if(data.getLicenseNumber().length()!=9){
            licenseNumber.setError("Incorrect license number");
        }else if(data.getDateofBirth().isEmpty()){
            dateofBirth.setError("incorrect date of birth");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(data.getEmail()).matches()){
            email.setError("Email is not valid");
        }else if(genderSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(this, "Incorrect gender", Toast.LENGTH_SHORT).show();
        }else if(licenseTypeSpinner.getSelectedItemPosition()==0)
        {
            Toast.makeText(this, "Incorrect License Type", Toast.LENGTH_SHORT).show();
        }else if(data.getAddress().length()<6){
            address.setError("address to short");
        }else {
            vali=true;
        }

        return vali;
    }

    private DriverType getDriverInputs(){

        DriverType temp=new DriverType();

        temp.setFirstName(firstName.getText().toString());
        temp.setLastName(lastName.getText().toString());
        temp.setCNIC(CNIC.getText().toString());
        temp.setLicenseNumber(licenseNumber.getText().toString());
        temp.setDateofBirth(dateofBirth.getText().toString());
        temp.setPhoneNumber(phoneNumber.getText().toString());
        temp.setGender(genderSpinner.getSelectedItem().toString());
        temp.setLicenseType(licenseTypeSpinner.getSelectedItem().toString());
        temp.setAddress(address.getText().toString());
        temp.setEmail(email.getText().toString());
        temp.setProfileLink("https://firebasestorage.googleapis.com/v0/b/" +
                "quick-parking-driver.appspot.com/o/IMG_20190611_203309_876" +
                ".jpg?alt=media&token=b0d33340-8a47-44a8-9439-0ba9f253e4c0");

        return temp;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.createNewDriver:

               final DriverType newDriver=getDriverInputs();


                if (driverDataValidation(newDriver)) {




                    database = FirebaseDatabase.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_DATABASE).getReference();



                        if(database!=null){

                            final ProgressDialog progressDialog=new ProgressDialog(NewDriver.this);
                            progressDialog.setTitle("Please wait. . .");
                            progressDialog.setTitle("Driver Data Saving in Server . . .");
                            progressDialog.show();
                            final UserRecord[] record = new UserRecord[1];
                            final Thread thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //Toast.makeText(NewDriver.this, "Added", Toast.LENGTH_SHORT).show();
                                        final UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                                                .setEmail(newDriver.getEmail())
                                                .setEmailVerified(false)
                                                .setPassword("123456")
                                                .setPhoneNumber(newDriver.getPhoneNumber())
                                                .setDisplayName(newDriver.getFirstName()+" "+newDriver.getLastName())
                                                .setPhotoUrl(newDriver.getProfileLink())
                                                .setDisabled(false);
                                        Log.d(TAG, "run: Crating user");
                                        record[0] = FirebaseAuth.getInstance(FirebaseInstances.FIREBASE_ParkingDriver_AUTH).createUser(request);
                                        newDriver.setUid(record[0].getUid());
                                        Log.d(TAG, "run: User Created");
                                        runOnUiThread(new Runnable() {
                                            
                                            @Override
                                            public void run() {

                                                Log.d(TAG, "run: now in database saving");
                                                database.child("Drivers Personal Information").child(record[0].getUid())
                                                        .setValue(newDriver, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError error, DatabaseReference ref) {


                                                                Log.d(TAG, "onComplete: Data and information is saved");
                                                                progressDialog.dismiss();
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        AlertDialog alertDialog =new AlertDialog.Builder(NewDriver.this)
                                                                                .setTitle("Driver Account")
                                                                                .setMessage("this account created successfully")
                                                                                .setCancelable(false)
                                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        finish();
                                                                                    }
                                                                                })
                                                                                .create();
                                                                        alertDialog.show();
                                                                    }
                                                                });

                                                            }
                                                        });

                                            }
                                        });


                                    } catch (FirebaseAuthException e) {
                                        Log.e(TAG, "run: "+e.getMessage());
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            thread.start();














                        }




                }


                break;
            case R.id.date_birth:

                Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();

                DatePicker datePicker=new DatePicker(NewDriver.this);
                DatePickerDialog datePickerDialog=new DatePickerDialog(NewDriver.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateofBirth.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                datePickerDialog.show();
                break;
        }
    }
}
